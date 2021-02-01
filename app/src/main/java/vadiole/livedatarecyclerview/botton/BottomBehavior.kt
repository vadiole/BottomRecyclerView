package vadiole.livedatarecyclerview.botton

interface BottomBehavior {
    fun extendBy(dY: Float): Boolean

    fun collapseBy(dY: Float): Boolean

    fun extend(velocity: Float): Boolean

    fun collapse(velocity: Float, onEnd: () -> Unit = {}): Boolean

}