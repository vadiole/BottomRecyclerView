package vadiole.bottomrecyclerview

import android.graphics.Point
import android.os.Build
import android.os.Bundle
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
import vadiole.bottomrecyclerview.botton.BottomDialog
import vadiole.bottomrecyclerview.list.OnItemClickListener


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: ViewModel by viewModels()
    private val viewConfig by lazy { ViewConfiguration.get(this) }

    private var bottomDialog: BottomDialog? = null
    private val onSwipeListener = object : SwipeableLayout.OnSwipeListener {
        override fun onSwipe(event: SwipeableLayout.SwipeEvent): Boolean {
            when (event.direction) {
                SwipeableLayout.SwipeEvent.UP -> {
                    when (event.action) {
                        ACTION_START -> {
                            return if (bottomDialog?.isAdded == true) {
                                bottomDialog?.extendBy(event.movedBy) == true
                            } else {
                                bottomDialog = BottomDialog().apply {
                                    supportFragmentManager.beginTransaction()
                                        .add(R.id.fragment_container, this)
                                        .commit()
                                }
                                true
                            }
                        }
                        ACTION_MOVE -> {
                            return bottomDialog?.extendBy(event.movedBy) == true
                        }
                        ACTION_END -> {
                            val showByPosition = event.movedBy > (screenHeight() * 0.5)
                            val showByVelocity =
                                (event.velocity!!) < -1 * viewConfig.scaledMinimumFlingVelocity
                            val hideByPosition = event.movedBy <= (screenHeight() * 0.5)
                            val hideByVelocity =
                                (event.velocity) > 1 * viewConfig.scaledMinimumFlingVelocity

                            when {
                                showByVelocity -> {
                                    bottomDialog?.extend(event.velocity)
                                }
                                hideByVelocity -> {
                                    bottomDialog?.collapse(event.velocity)
                                }
                                showByPosition -> {
                                    bottomDialog?.extend(event.velocity)
                                }
                                hideByPosition -> {
                                    bottomDialog?.collapse(event.velocity)
                                }
                            }
                            return true
                        }
                    }
                }
                SwipeableLayout.SwipeEvent.DOWN -> {
                    when (event.action) {
                        ACTION_START -> {
                            if (bottomDialog?.isAdded == false) return false
                            return bottomDialog?.collapseBy(event.movedBy) == true
                        }
                        ACTION_MOVE -> {
                            return bottomDialog?.collapseBy(event.movedBy) == true
                        }
                        ACTION_END -> {
                            val showByPosition = event.movedBy > (screenHeight() * 0.5)
                            val showByVelocity =
                                (event.velocity!!) < -1 * viewConfig.scaledMinimumFlingVelocity
                            val hideByPosition = event.movedBy <= (screenHeight() * 0.5)
                            val hideByVelocity =
                                (event.velocity) > 1 * viewConfig.scaledMinimumFlingVelocity

                            when {
                                showByVelocity -> {
                                    bottomDialog?.extend(event.velocity)
                                }
                                hideByVelocity -> {
                                    bottomDialog?.collapse(event.velocity)
                                }
                                showByPosition -> {
                                    bottomDialog?.extend(event.velocity)
                                }
                                hideByPosition -> {
                                    bottomDialog?.collapse(event.velocity)
                                }
                            }
                            return true
                        }
                    }
                }
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.toolbar).setOnClickListener {
//            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
//            BottomDialog()
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container, BottomDialog())
//                .commit()
        }


        findViewById<SwipeableLayout>(R.id.root).apply {
            onSwipeListener = this@MainActivity.onSwipeListener

        }

    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "onItemClick: $position", Toast.LENGTH_SHORT).show()
    }


    var actionDownX = -1f
    var actionDownY = -1f
    var isDragging = false


    fun screenHeight() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        windowManager?.run {
            currentWindowMetrics.bounds.height()
        } ?: 0
    } else {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        size.y
    }
}
