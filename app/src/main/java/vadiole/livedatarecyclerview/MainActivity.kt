package vadiole.livedatarecyclerview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import vadiole.SwipeableLayout
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_END
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_MOVE
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_START
import vadiole.livedatarecyclerview.botton.BottomDialog
import vadiole.livedatarecyclerview.list.OnItemClickListener


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: ViewModel by viewModels()
    private val touchSlop by lazy { ViewConfiguration.get(this).scaledTouchSlop}

    private var bottomDialog: BottomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.toolbar).setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            BottomDialog()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, BottomDialog())
                .commit()
        }


        findViewById<SwipeableLayout>(R.id.root).apply {
            onSwipeListener = object : SwipeableLayout.OnSwipeListener {
                override fun onSwipe(event: SwipeableLayout.SwipeEvent): Boolean {
                    Log.i("Main", "onSwipe: $event")
                    if (event.direction != SwipeableLayout.SwipeEvent.UP) return false

                    when (event.action) {
                        ACTION_START -> {
                            bottomDialog = BottomDialog().apply {
                                supportFragmentManager.beginTransaction()
                                    .add(R.id.fragment_container, this)
                                    .commit()
                            }
                        }
                        ACTION_MOVE -> {
                            bottomDialog?.animateBySwipe(event.movedBy)
                        }
                        ACTION_END -> {
                            bottomDialog?.let {
                                supportFragmentManager.beginTransaction().remove(it)
                            }
                        }
                    }
                    return true
                }
            }
        }
//
//        val adapter = Adapter(this, this)
//        val layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false )
//        val recycler = findViewById<RecyclerView>(R.id.recycler_view)
//
//        val callback = ItemTouchHelperCallback(adapter)
//        ItemTouchHelper(callback).attachToRecyclerView(recycler)
//
//        recycler.adapter = adapter
//        recycler.layoutManager = layoutManager
//        recycler.setHasFixedSize(true)
//
//
//
//        viewModel.list.observe(this) {
//            Log.i("Main", "update list")
//            adapter.submitList(it)
//        }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "onItemClick: $position", Toast.LENGTH_SHORT).show()
    }


    var actionDownX = -1f
    var actionDownY = -1f
    var isDragging = false


}
