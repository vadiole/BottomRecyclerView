package vadiole.livedatarecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vadiole.livedatarecyclerview.model.Item1
import vadiole.livedatarecyclerview.model.Item2

class Adapter(
    private val list: List<Any>, private val lifecycleOwner: LifecycleOwner,
    private val listener: ItemClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_1 -> ItemHolder1(inflater, parent)
            TYPE_2 -> ItemHolder2(inflater, parent)
            else -> throw NotImplementedError()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        when (holder.itemViewType) {
            TYPE_1 -> (holder as ItemHolder1).bind(item as Item1, lifecycleOwner, listener)
            TYPE_2 -> (holder as ItemHolder2).bind(listener)
            else -> throw NotImplementedError()
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = when (list[position]) {
        is Item1 -> 1
        is Item2 -> 2
        else -> throw NotImplementedError()
    }


    class ItemHolder1(inflater: LayoutInflater, parent: ViewGroup) :

        ViewHolder(
            inflater.inflate(
                R.layout.item1,
                parent,
                false
            )
        ) {

        private var titleView: TextView
        private var timeView: TextView

        init {
            with(itemView) {
                titleView = findViewById(R.id.title)
                timeView = findViewById(R.id.time)
            }
        }

        fun bind(item: Item1, lifecycleOwner: LifecycleOwner, listener: ItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
            with(item) {
                titleView.text = title
                time.observe(lifecycleOwner) { time ->
                    timeView.text = time
                }
            }
        }
    }


    class ItemHolder2(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(inflater.inflate(R.layout.item2, parent, false)) {

        fun bind(listener: ItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
        }
    }


    companion object {
        const val TYPE_1 = 1
        const val TYPE_2 = 2
    }

}