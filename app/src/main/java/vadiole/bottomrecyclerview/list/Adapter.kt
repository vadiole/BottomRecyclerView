package vadiole.bottomrecyclerview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*


class Adapter(
    private val lifecycleOwner: LifecycleOwner,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<ViewHolder>(), ItemTouchHelperAdapter {

    private val differCallback = object : DiffUtil.ItemCallback<Equatable>() {
        override fun areItemsTheSame(old: Equatable, new: Equatable) = when {
            old is Item1 && new is Item1 -> old.id == new.id
            old is Item2 && new is Item2 -> true
            else -> false
        }

        override fun areContentsTheSame(old: Equatable, new: Equatable) = when {
            old is Item1 && new is Item1 -> old == new
            old is Item2 && new is Item2 -> old == new
            else -> false
        }
    }

    //
    private val differ: AsyncListDiffer<Equatable> = AsyncListDiffer(this, differCallback)

    private val currentList: List<Equatable>
        get() = differ.currentList

    override fun getItemCount() = differ.currentList.size

    private fun getItem(position: Int) = differ.currentList[position]

    fun submitList(list: List<Equatable>) = differ.submitList(list.toList())
//
//    private val differ: AsyncListDiffer<Equatable> = AsyncListDiffer(this, differCallback)

//    var currentList = mutableListOf<Equatable>()
//    override fun getItemCount() = currentList.size
//
//    private fun getItem(position: Int) = currentList[position]
//
//    fun submitList(list: List<Equatable>) {
//        currentList = list.toMutableList()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_1 -> ItemHolder1(LayoutInflater.from(parent.context), parent)
            TYPE_2 -> ItemHolder2(LayoutInflater.from(parent.context), parent)
            else -> throw NotImplementedError()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder.itemViewType) {
            TYPE_1 -> (holder as ItemHolder1).bind(item as Item1, lifecycleOwner, listener)
            TYPE_2 -> (holder as ItemHolder2).bind(listener)
            else -> throw NotImplementedError()
        }
    }


    override fun getItemViewType(position: Int) = when {
        getItem(position) is Item1 -> 1
        getItem(position) is Item2 -> 2
        else -> throw NotImplementedError()
    }

    override fun getItemId(position: Int): Long {
        return (currentList[position] as? Item1)?.id?.toLong()
            ?: (currentList[position] as? Item2)!!.id.toLong()
    }

    companion object {
        const val TYPE_1 = 1
        const val TYPE_2 = 2
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val mItems = currentList.toMutableList()

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mItems, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mItems, i, i - 1)
            }
        }
        submitList(mItems)
//        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) = Unit
}
