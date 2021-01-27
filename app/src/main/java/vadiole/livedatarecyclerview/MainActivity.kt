package vadiole.livedatarecyclerview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import vadiole.livedatarecyclerview.model.list.ItemTouchHelperCallback


class MainActivity : AppCompatActivity(), OnItemClickListener {

    val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = Adapter(this@MainActivity, this@MainActivity)
        val recycler = findViewById<RecyclerView>(R.id.recycler_view)

        val callback = ItemTouchHelperCallback(adapter)
        ItemTouchHelper(callback).attachToRecyclerView(recycler)

        recycler.adapter = adapter
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
