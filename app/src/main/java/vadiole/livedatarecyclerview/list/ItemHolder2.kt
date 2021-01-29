package vadiole.livedatarecyclerview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vadiole.livedatarecyclerview.R

class ItemHolder2(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item2, parent, false)),
    ItemTouchHelperViewHolder {

    fun bind(listener: OnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
    }

    override fun onItemSelected() = Unit

    override fun onItemClear() = Unit
}

