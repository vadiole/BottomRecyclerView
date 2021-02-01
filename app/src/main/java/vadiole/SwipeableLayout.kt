package vadiole

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_END
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_MOVE
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_NONE
import vadiole.SwipeableLayout.SwipeEvent.Companion.ACTION_START
import vadiole.SwipeableLayout.SwipeEvent.Companion.DOWN
import vadiole.SwipeableLayout.SwipeEvent.Companion.LEFT
import vadiole.SwipeableLayout.SwipeEvent.Companion.RIGHT
import vadiole.SwipeableLayout.SwipeEvent.Companion.UP
import kotlin.math.abs

class SwipeableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    var onSwipeListener: OnSwipeListener? = null
        set(value) = run { field = value }


    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop


    private var velocityTracker: VelocityTracker? = null

    private var currentAction = ACTION_NONE
    private var direction = -1
    private var isSwiping = false
    private var startX = -1f
    private var startY = -1f


    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentAction = ACTION_NONE

                startX = event.rawX
                startY = event.rawY

                velocityTracker?.clear()
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                velocityTracker?.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (currentAction == ACTION_MOVE) return true
                if (currentAction == ACTION_NONE) {
                    velocityTracker?.addMovement(event)

                    val x = event.rawX
                    val y = event.rawY

                    val dX = startX - x
                    val dY = startY - y


                    if (abs(dX) > abs(dY)) {
                        if (abs(dX) > touchSlop) {
                            val direction = if (dX > 0) LEFT else RIGHT
                            val swipeEvent = SwipeEvent(ACTION_START, direction, abs(dX), null)
                            return if (onSwipeListener?.onSwipe(swipeEvent) == true) {
                                currentAction = ACTION_MOVE
                                true
                            } else false
                        }
                    } else {
                        if (abs(dY) > touchSlop) {
                            val direction = if (dY > 0) UP else DOWN
                            val swipeEvent = SwipeEvent(ACTION_START, direction, abs(dY), null)
                            return if (onSwipeListener?.onSwipe(swipeEvent) == true) {
                                currentAction = ACTION_MOVE
                                true
                            } else false
                        }
                    }
                }
            }


        }

        return super.onInterceptTouchEvent(event)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentAction != ACTION_MOVE) return super.onTouchEvent(event)
        if (onSwipeListener == null) return super.onTouchEvent(event)

        val movedBy = when (direction) {
            UP -> startY - event.rawY
            DOWN -> event.rawY - startY
            LEFT -> startX - event.rawX
            RIGHT -> event.rawX - startX
            else -> return false
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(event)

                val swipeEvent = if (movedBy < 0f) {
                    currentAction = ACTION_END
                    SwipeEvent(ACTION_END, direction, 0f, null)
                } else {
                    SwipeEvent(ACTION_MOVE, direction, movedBy, null)
                }
                onSwipeListener?.onSwipe(swipeEvent)

                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val pointerId: Int = event.getPointerId(event.actionIndex)
                val velocity = if (direction == UP || direction == DOWN) {
                    velocityTracker?.getYVelocity(pointerId) ?: 0f
                } else {
                    velocityTracker?.getXVelocity(pointerId) ?: 0f
                }

                val swipeEvent = SwipeEvent(ACTION_MOVE, direction, movedBy, velocity)
                onSwipeListener?.onSwipe(swipeEvent)

                currentAction = ACTION_NONE
                velocityTracker?.recycle()
                velocityTracker = null
                return true
            }
        }

        return super.onTouchEvent(event)
    }


    interface OnSwipeListener {
        fun onSwipe(event: SwipeEvent): Boolean
    }

    data class SwipeEvent(
        val action: Int,
        val direction: Int,
        val movedBy: Float,
        val velocity: Float?
    ) {
        companion object {
            const val UP = 0
            const val DOWN = 1
            const val LEFT = 2
            const val RIGHT = 3

            const val ACTION_START = 4
            const val ACTION_MOVE = 5
            const val ACTION_END = 6
            const val ACTION_NONE = 7

        }
    }


}
