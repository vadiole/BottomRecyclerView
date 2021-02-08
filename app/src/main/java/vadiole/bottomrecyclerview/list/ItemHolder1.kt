package vadiole.bottomrecyclerview.list

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import vadiole.bottomrecyclerview.R
import vadiole.bottomrecyclerview.botton.removeSelf

class ItemHolder1(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_stub, parent, false)
    ),
    ItemTouchHelperViewHolder {

    private var isInflated = false
    private var titleView: TextView? = null
    private var timeView: TextView? = null

    private var titleValue: String? = null
    private var timeValue: String? = null

    init {
        val context = parent.context
        val asyncInflater = AsyncLayoutInflater(context)
        val callback = AsyncLayoutInflater.OnInflateFinishedListener { view, resid, viewGroup ->

            with(itemView) {
                handler?.post {
                    viewGroup?.addView(view)
                    findViewById<View>(R.id.stub).removeSelf()
                    findViewById<ViewGroup>(R.id.root_async).clipToOutline = true
                    titleView = findViewById(R.id.title)
                    timeView = findViewById(R.id.time)
                    isInflated = true

                    titleValue?.let { titleView?.text = it }
                    timeValue?.let { timeView?.text = it }
                } ?: run {
                    viewGroup?.addView(view)
                    findViewById<View>(R.id.stub).removeSelf()
                    findViewById<ViewGroup>(R.id.root_async).clipToOutline = true
                    titleView = findViewById(R.id.title)
                    timeView = findViewById(R.id.time)
                    isInflated = true

                    titleValue?.let { titleView?.text = it }
                    timeValue?.let { timeView?.text = it }
                }

            }
        }
        asyncInflater.inflate(R.layout.item1, itemView as ViewGroup, callback)
    }

    fun bind(item: Item1, listener: OnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
        with(item) {
            if (isInflated) {
                titleView?.text = title
                timeView?.text = time
            } else {
                titleValue = title
                timeValue = time
            }
        }
    }

    override fun onItemSelected() = with(itemView) {
        performHapticFeedback(
            HapticFeedbackConstants.KEYBOARD_TAP,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
        val lastScaleX = scaleX
        val lastScaleY = scaleY
        val lastAlpha = alpha

        clearAnimation()

        scaleX = lastScaleX
        scaleY = lastScaleY
        alpha = lastAlpha

        animate()
            .withLayer()
            .setDuration(100)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .alpha(0.8f)
            .setInterpolator(AccelerateInterpolator())
            .start()

        Unit
    }

    override fun onItemClear() = with(itemView) {
        val lastScaleX = scaleX
        val lastScaleY = scaleY
        val lastAlpha = alpha

        clearAnimation()

        scaleX = lastScaleX
        scaleY = lastScaleY
        alpha = lastAlpha

        animate()
            .withLayer()
            .setDuration(100)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1f)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}

