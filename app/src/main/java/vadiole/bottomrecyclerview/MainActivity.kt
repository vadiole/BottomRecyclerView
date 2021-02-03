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
import vadiole.bottomrecyclerview.botton.BottomFragment
import vadiole.bottomrecyclerview.list.OnItemClickListener


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: ViewModel by viewModels()
    private val viewConfig by lazy { ViewConfiguration.get(this) }

    private var bottomFragment: BottomFragment? = null
    private val onSwipeListener = object : SwipeableLayout.OnSwipeListener {
        override fun onSwipe(event: SwipeableLayout.SwipeEvent): Boolean {
            when (event.direction) {
                SwipeableLayout.SwipeEvent.UP -> {
                    when (event.action) {
                        ACTION_START -> with(supportFragmentManager) {
                            val fragment = (bottomFragment
                                ?: findFragmentByTag(BottomFragment.TAG) as? BottomFragment
                                ?: BottomFragment()).also { bottomFragment = it }

                            if (fragment.isAdded) return fragment.extendBy(event.movedBy) else {
                                beginTransaction().replace(
                                    R.id.fragment_container,
                                    fragment,
                                    BottomFragment.TAG
                                ).commit()
                                return true
                            }
                        }
                        ACTION_MOVE -> {
                            return bottomFragment?.extendBy(event.movedBy) == true
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
                                    bottomFragment?.extend(event.velocity)
                                }
                                hideByVelocity -> {
                                    bottomFragment?.collapse(event.velocity)
                                }
                                showByPosition -> {
                                    bottomFragment?.extend(event.velocity)
                                }
                                hideByPosition -> {
                                    bottomFragment?.collapse(event.velocity)
                                }
                            }
                            return true
                        }
                    }
                }
                SwipeableLayout.SwipeEvent.DOWN -> {
                    when (event.action) {
                        ACTION_START -> with(supportFragmentManager) {

                            val fragment = (bottomFragment
                                ?: findFragmentByTag(BottomFragment.TAG) as? BottomFragment
                                ?: BottomFragment()).also { bottomFragment = it }

                            if (!fragment.isAdded) return false
                            return fragment.collapseBy(event.movedBy)
                        }
                        ACTION_MOVE -> {
                            return bottomFragment?.collapseBy(event.movedBy) == true
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
                                    bottomFragment?.extend(event.velocity)
                                }
                                hideByVelocity -> {
                                    bottomFragment?.run {
                                        collapse(event.velocity) {
                                            supportFragmentManager
                                                .beginTransaction()
                                                .remove(this)
                                                .commit()
                                        }
                                    }
                                }
                                showByPosition -> {
                                    bottomFragment?.extend(event.velocity)
                                }
                                hideByPosition -> {
                                    bottomFragment?.run {
                                        collapse(event.velocity) {
                                            supportFragmentManager
                                                .beginTransaction()
                                                .remove(this)
                                                .commit()
                                        }
                                    }
                                    bottomFragment = null
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
