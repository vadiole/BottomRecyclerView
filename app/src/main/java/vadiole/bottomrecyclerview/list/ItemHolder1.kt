package vadiole.bottomrecyclerview.list

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import vadiole.bottomrecyclerview.R

class ItemHolder1(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item1, parent, false)),
    ItemTouchHelperViewHolder {

    private var titleView: TextView
    private var timeView: TextView

    init {
        with(itemView) {
            titleView = findViewById(R.id.title)
            timeView = findViewById(R.id.time)
        }
    }

    fun bind(item: Item1, lifecycleOwner: LifecycleOwner, listener: OnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
        with(item) {
            titleView.text = title
//                time.observe(lifecycleOwner) { time ->
//                    timeView.text = time
//                }
            timeView.text = time
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
            .setDuration(100)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1f)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}

