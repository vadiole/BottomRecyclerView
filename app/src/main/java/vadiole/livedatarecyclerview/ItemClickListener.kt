package vadiole.livedatarecyclerview

import android.view.View

interface ItemClickListener {
    fun onItemClick(view: View, position: Int)
}