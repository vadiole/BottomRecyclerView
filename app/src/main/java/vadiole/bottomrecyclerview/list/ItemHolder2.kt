package vadiole.bottomrecyclerview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vadiole.bottomrecyclerview.R

class ItemHolder2(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item2, parent, false)),
    ItemTouchHelperViewHolder {

    fun bind(listener: OnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
    }

    override fun onItemSelected() = Unit

    override fun onItemClear() = Unit
}

