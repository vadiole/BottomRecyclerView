package vadiole.bottomrecyclerview.botton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator



val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


val Float.toDp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


fun View?.removeSelf() {
    this ?: return
    val parent = parent as? ViewGroup ?: return
    parent.removeView(this)
}
