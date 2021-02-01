package vadiole.livedatarecyclerview.botton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import vadiole.livedatarecyclerview.R
import vadiole.livedatarecyclerview.list.Adapter

class BottomDialog : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.translationY = view.height.toFloat()
    }

    fun animateBySwipe(dY: Float) {
        view?.apply {
            translationY = height - dY
        }
    }
}
