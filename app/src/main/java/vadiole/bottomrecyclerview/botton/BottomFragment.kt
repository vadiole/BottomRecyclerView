package vadiole.bottomrecyclerview.botton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import vadiole.bottomrecyclerview.R
import vadiole.bottomrecyclerview.ViewModel
import vadiole.bottomrecyclerview.list.Adapter
import vadiole.bottomrecyclerview.list.ItemTouchHelperCallback
import vadiole.bottomrecyclerview.list.OnItemClickListener
import kotlin.math.abs

class BottomFragment : Fragment(), BottomBehavior, OnItemClickListener {

    private val viewModel: ViewModel by activityViewModels()
    private val layoutAnimation by lazy {
        AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
    }
    private var recycler: RecyclerView? = null
    private var mAdapter: Adapter? = null

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, state: Bundle?): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_bottom, c, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val asyncInflater = AsyncLayoutInflater(requireContext())
        val container = view as ViewGroup
        asyncInflater.inflate(R.layout.fragment_bottom_content, container) { v, _, parent ->
            getView()?.post {
                (parent as ViewGroup).addView(v)
                val mLayoutManager = GridLayoutManager(requireContext(), 2, VERTICAL, false)
                mAdapter = Adapter(this).apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }
                recycler = v.findViewById(R.id.recycler_view)

                val callback = ItemTouchHelperCallback(mAdapter!!)
                ItemTouchHelper(callback).attachToRecyclerView(recycler)

                recycler?.apply {
                    adapter = mAdapter
                    layoutManager = mLayoutManager
                    setHasFixedSize(true)
                }

                viewModel.list.observe(viewLifecycleOwner) { list ->
                    if (mAdapter?.itemCount == 0) {
                        if (viewModel.isExtended) {
                            recycler?.layoutAnimation = layoutAnimation
                            recycler?.scheduleLayoutAnimation()
                        } else {
                            recycler?.layoutAnimation = null
                        }
                    }
                    mAdapter?.submitList(list)
                }
            }
        }

        view.doOnNextLayout {
            it.translationY = it.height.toFloat()
        }

        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun extendBy(dY: Float): Boolean {
        if (!isAdded) return true
        if (viewModel.isAnimating) return false
        if (viewModel.isExtended) return false
        if (viewModel.isExtended && recycler?.canScrollVertically(-1) == true) return false

        view?.apply {
            translationY = height - dY
            return true
        }
        return false
    }

    override fun collapseBy(dY: Float): Boolean {
        if (!isAdded) return true
        if (viewModel.isAnimating) return false
        if (!viewModel.isExtended) return false
        if (viewModel.isExtended && recycler?.canScrollVertically(-1) == true) return false

        view?.apply {
            translationY = dY
            return true
        }
        return false
    }

    override fun extend(velocity: Float): Boolean {
        if (!isAdded) return true
        if (viewModel.isAnimating) return false

        view?.apply {
            val duration = 1.5 * 200 + 10000 / (abs(velocity) + 300).toDp
            viewModel.isAnimating = true

            doOnLayout {
                animate().withLayer()
                    .setDuration(duration.toLong())
                    .translationY(0f)
                    .setInterpolator(LinearOutSlowInInterpolator())
                    .withEndAction { viewModel.isAnimating = false; viewModel.isExtended = true }
                    .start()
            }
            return true
        }

        return false
    }

    override fun collapse(velocity: Float, onEnd: () -> Unit): Boolean {
        if (!isAdded) return true
        if (viewModel.isAnimating) return false

        view?.apply {
            val duration = 1.5 * 200 + 10000 / (abs(velocity) + 300).toDp

            animate().withLayer()
                .setDuration(duration.toLong())
                .translationY(height.toFloat())
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    viewModel.isAnimating = false; viewModel.isExtended = false; onEnd.invoke()
                }
                .start()
            return true
        }
        return false
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(requireContext(), "item $position click", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "BottomDialog"
    }
}
