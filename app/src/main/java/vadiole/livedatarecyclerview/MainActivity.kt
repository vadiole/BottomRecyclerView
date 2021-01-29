package vadiole.livedatarecyclerview

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import vadiole.livedatarecyclerview.botton.BottomDialog
import vadiole.livedatarecyclerview.list.OnItemClickListener
import kotlin.math.hypot


class MainActivity : AppCompatActivity(), View.OnTouchListener, OnItemClickListener {

    private val viewModel: ViewModel by viewModels()
    private val touchSlop = ViewConfiguration.get(this).scaledTouchSlop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ConstraintLayout>(R.id.root).apply { 
            setOnDragListener { v, event ->  }
        }
        findViewById<TextView>(R.id.toolbar).setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            BottomDialog()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, BottomDialog())
                .commit()

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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isDragging) return true
        return super.dispatchTouchEvent(ev)
    }
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v.id == R.id.root) {
            return consumeTouch(event)
        }
        return false
    }




    fun consumeTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                actionDownX = event.rawX
                actionDownY = event.rawY
                return false
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.rawX
                val y = event.rawY
                val dist = hypot(x - actionDownX, y - actionDownY)
                if (dist >)
            }

            MotionEvent.ACTION_UP -> {

            }
        }
    }


}
