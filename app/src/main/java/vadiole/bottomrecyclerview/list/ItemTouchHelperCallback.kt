package vadiole.bottomrecyclerview.list

import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vadiole.bottomrecyclerview.botton.toPx
import vadiole.bottomrecyclerview.list.Adapter.Companion.TYPE_1
import vadiole.bottomrecyclerview.list.Adapter.Companion.TYPE_2
import kotlin.math.abs
import kotlin.math.sign

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

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        val itemTouchViewHolder = viewHolder as? ItemTouchHelperViewHolder ?: return
        if (actionState == ACTION_STATE_DRAG) {
            itemTouchViewHolder.onItemSelected()
        }
    }

    override fun interpolateOutOfBoundsScroll(
        recyclerView: RecyclerView,
        viewSize: Int,
        viewSizeOutOfBounds: Int,
        totalSize: Int,
        msSinceStartScroll: Long
    ): Int {
        val direction = sign(viewSizeOutOfBounds.toDouble()).toInt()
        return 10.toPx * direction
    }



    //      0.5 >= x >= 1 (if less than 0.5, it will shake due to constant overlap)
    val DRAG_THRESHOLD_PERSENT = 0.5
    override fun chooseDropTarget(
        selected: ViewHolder,
        targets: MutableList<ViewHolder>,
        curX: Int, curY: Int
    ): ViewHolder? {
        val verticalOffset = (selected.itemView.height * DRAG_THRESHOLD_PERSENT).toInt()
        val horizontalOffset = (selected.itemView.width * DRAG_THRESHOLD_PERSENT).toInt()
        val left = curX - horizontalOffset
        val right = curX + selected.itemView.width + horizontalOffset
        val top = curY - verticalOffset
        val bottom = curY + selected.itemView.height + verticalOffset
        var winner: ViewHolder? = null
        var winnerScore = -1
        val dx = curX - selected.itemView.left
        val dy = curY - selected.itemView.top
        val targetsSize = targets.size
        for (i in 0 until targetsSize) {
            val target = targets[i]
            if (dx > 0) {
                val diff = target.itemView.right - right
                if (diff < 0 && target.itemView.right > selected.itemView.right) {
                    val score = abs(diff)
                    if (score > winnerScore) {
                        winnerScore = score
                        winner = target
                    }
                }
            }
            if (dx < 0) {
                val diff = target.itemView.left - left
                if (diff > 0 && target.itemView.left < selected.itemView.left) {
                    val score = abs(diff)
                    if (score > winnerScore) {
                        winnerScore = score
                        winner = target
                    }
                }
            }
            if (dy < 0) {
                val diff = target.itemView.top - top
                if (diff > 0 && target.itemView.top < selected.itemView.top) {
                    val score = abs(diff)
                    if (score > winnerScore) {
                        winnerScore = score
                        winner = target
                    }
                }
            }
            if (dy > 0) {
                val diff = target.itemView.bottom - bottom
                if (diff < 0 && target.itemView.bottom > selected.itemView.bottom) {
                    val score = abs(diff)
                    if (score > winnerScore) {
                        winnerScore = score
                        winner = target
                    }
                }
            }
        }
        return winner
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: ViewHolder,
        target: ViewHolder
    ): Boolean {
        if (target.itemViewType == TYPE_2) return false
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val itemTouchViewHolder = viewHolder as? ItemTouchHelperViewHolder ?: return
        itemTouchViewHolder.onItemClear()
    }
}
