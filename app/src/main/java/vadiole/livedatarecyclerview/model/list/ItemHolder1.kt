package vadiole.livedatarecyclerview.model.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import vadiole.livedatarecyclerview.OnItemClickListener
import vadiole.livedatarecyclerview.R
import vadiole.livedatarecyclerview.model.Item1

class ItemHolder1(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item1, parent, false)) {

    private var titleView: TextView
    private var timeView: TextView

    init {
        with(itemView) {
            titleView = findViewById(R.id.title)
            timeView = findViewById(R.id.time)
        }
    }

    fun bind(item: Item1, lifecycleOwner: LifecycleOwner, listener: OnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(it, bindingAdapterPosition) }
        with(item) {
            titleView.text = title
//                time.observe(lifecycleOwner) { time ->
//                    timeView.text = time
//                }
            timeView.text = time
        }
    }
}

