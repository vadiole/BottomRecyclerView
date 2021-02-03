package vadiole.bottomrecyclerview.botton

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import vadiole.bottomrecyclerview.R
import vadiole.bottomrecyclerview.ViewModel
import vadiole.bottomrecyclerview.list.Adapter
import vadiole.bottomrecyclerview.list.ItemTouchHelperCallback
import vadiole.bottomrecyclerview.list.OnItemClickListener
import kotlin.math.abs

class BottomFragment : Fragment(), BottomBehavior, OnItemClickListener {

    private val viewModel: ViewModel by activityViewModels()
    private var recycler: RecyclerView? = null
    private var mAdapter: Adapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.dialog_bottom, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.translationY = view.height.toFloat()
        view.doOnPreDraw {
            startPostponedEnterTransition()

            val mLayoutManager =
                GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            mAdapter = Adapter(this, this).apply {
                stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            recycler = it.findViewById(R.id.recycler_view)

            val callback = ItemTouchHelperCallback(mAdapter!!)
            ItemTouchHelper(callback).attachToRecyclerView(recycler)

            recycler?.apply {
                adapter = mAdapter
                layoutManager = mLayoutManager
                setHasFixedSize(true)
            }


            viewModel.list.observe(viewLifecycleOwner) { list ->
                Log.i("Main", "update list")
                if (mAdapter?.itemCount == 0) {
                    recycler?.scheduleLayoutAnimation()
                }
                mAdapter?.submitList(list)
            }
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
            val duration = 200 + 10000 / (abs(velocity) + 300).toDp
            Log.d("SWIPE", "extend duration: $duration, velocity: $velocity")

            viewModel.isAnimating = true
            animate()
                .setDuration(duration.toLong())
                .translationY(0f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .onEnd { viewModel.isAnimating = false; viewModel.isExtended = true }
                .start()
            return true
        }


        return false
    }

    override fun collapse(velocity: Float, onEnd: () -> Unit): Boolean {
        if (!isAdded) return true
        if (viewModel.isAnimating) return false

        view?.apply {
            val duration = 200 + 10000 / (abs(velocity) + 300).toDp
            Log.d("SWIPE", "collapse duration: $duration, velocity: $velocity")
            animate()
                .setDuration(duration.toLong())
                .translationY(height.toFloat())
                .setInterpolator(LinearOutSlowInInterpolator())
                .onEnd {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach")
    }

    companion object {
        const val TAG = "BottomDialog"
    }
}
