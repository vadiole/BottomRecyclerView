package vadiole.bottomrecyclerview.botton

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

class BottomDialog : Fragment(), BottomBehavior, OnItemClickListener {

    private val viewModel: ViewModel by viewModels()
    private var recycler: RecyclerView? = null
    private var mAdapter: Adapter? = null

    private var isAnimating = false
    private var isExtended = false

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
            mAdapter = Adapter(this, this)
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
                mAdapter?.submitList(list)
                recycler?.scheduleLayoutAnimation()
            }
        }
    }


    override fun extendBy(dY: Float): Boolean {
        if (isAnimating) return false
        if (isExtended) return false
        if (recycler?.canScrollVertically(-1) == true) return false
        view?.apply {
            translationY = height - dY
        }
        return true
    }

    override fun collapseBy(dY: Float): Boolean {
        if (isAnimating) return false
        if (!isExtended) return false
        if (recycler?.canScrollVertically(-1) == true) return false


        view?.apply {
            translationY = dY
        }
        return true
    }

    override fun extend(velocity: Float): Boolean {
        if (isAnimating) return false

        view?.apply {
            val duration = 200 + 10000 / (abs(velocity) + 300).toDp
            Log.d("SWIPE", "extend duration: $duration, velocity: $velocity")

            isAnimating = true
            animate()
                .setDuration(duration.toLong())
                .translationY(0f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .onEnd { isAnimating = false; isExtended = true }
                .start()
            return true
        }


        return false
    }

    override fun collapse(velocity: Float, onEnd: () -> Unit): Boolean {
        if (isAnimating) return false

        view?.apply {
            val duration = 200 + 10000 / (abs(velocity) + 300).toDp
            Log.d("SWIPE", "collapse duration: $duration, velocity: $velocity")
            animate()
                .setDuration(duration.toLong())
                .translationY(height.toFloat())
                .setInterpolator(LinearOutSlowInInterpolator())
                .onEnd { isAnimating = false; isExtended = false; onEnd.invoke() }
                .start()
            return true
        }
        return false
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(requireContext(), "item $position click", Toast.LENGTH_SHORT).show()
    }


}
