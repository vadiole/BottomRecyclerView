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
import vadiole.SwipeableLayout.SwipeEvent.Companion.DOWN
import vadiole.SwipeableLayout.SwipeEvent.Companion.LEFT
import vadiole.SwipeableLayout.SwipeEvent.Companion.RIGHT
import vadiole.SwipeableLayout.SwipeEvent.Companion.UP

class SwipeableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var onSwipeListener: OnSwipeListener? = null
        set(value) = run { field = value }


    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop


    private var velocityTracker: VelocityTracker? = null

    private var action = ACTION_NONE
    private var direction = -1
    private var isSwiping = false
    private var actionDownX = -1f
    private var actionDownY = -1f


    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker?.clear()
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                velocityTracker?.addMovement(event)

                actionDownX = event.rawX
                actionDownY = event.rawY
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (action != ACTION_MOVE) return super.onTouchEvent(event)
        if (onSwipeListener == null) return super.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(event)


                val movedBy = when (direction) {
                    UP -> actionDownY - event.rawY
                    DOWN -> event.rawY - actionDownY
                    LEFT -> actionDownX - event.rawX
                    RIGHT -> event.rawX - actionDownX
                    else -> return false
                }

                if (movedBy < 0f) {
                    val swipeEvent = SwipeEvent(ACTION_END, direction, 0f, Float.NaN)
                    onSwipeListener?.onSwipe(swipeEvent)

                } else {
                    val swipeEvent = SwipeEvent(ACTION_MOVE, direction, movedBy, Float.NaN)
                    onSwipeListener?.onSwipe(swipeEvent)
                }

                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val pointerId: Int = event.getPointerId(event.actionIndex)
                val velocity = velocityTracker?.getXVelocity(pointerId) ?: 0f
                onSwipeListener?.onSwipe(event)
                action = ACTION_NONE
                velocityTracker?.recycle()
                velocityTracker = null
                return true
            }
        }

        return super.onTouchEvent(event)
    }


    interface OnSwipeListener {
        fun onSwipe(event: SwipeEvent)
    }

    data class SwipeEvent(
        val action: Int,
        val direction: Int,
        val movedBy: Float,
        val velocity: Float
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