package vadiole.bottomrecyclerview.botton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Resources
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.onEnd(action: () -> Unit): ViewPropertyAnimator {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            action.invoke()
        }
    })
    return this
}

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


val Float.toDp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)
