package vadiole.livedatarecyclerview.model.list

import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vadiole.livedatarecyclerview.Adapter.Companion.TYPE_1

class ItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) : Callback() {
    override fun isLongPressDragEnabled() = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        return if (viewHolder.itemViewType == TYPE_1) {
            val dragFlags = UP or DOWN or LEFT or RIGHT
            makeMovementFlags(dragFlags, 0)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    override fun onMove(recyclerView: RecyclerView, from: ViewHolder, to: ViewHolder): Boolean {
        val fromPosition = from.bindingAdapterPosition
        val toPosition = to.bindingAdapterPosition
        return adapter.onItemMove(fromPosition, toPosition)
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) = Unit
}
