package vadiole.livedatarecyclerview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import vadiole.livedatarecyclerview.model.list.ItemTouchHelperCallback


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = Adapter(this, this)
        val layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false )
        val recycler = findViewById<RecyclerView>(R.id.recycler_view)

        val callback = ItemTouchHelperCallback(adapter)
        ItemTouchHelper(callback).attachToRecyclerView(recycler)

        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
        recycler.setHasFixedSize(true)



        viewModel.list.observe(this) {
            Log.i("Main", "update list")
            adapter.submitList(it)
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "onItemClick: $position", Toast.LENGTH_SHORT).show()
    }
}
