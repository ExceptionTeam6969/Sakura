package dev.exceptionteam.sakura.graphics.font

data class CharData(
    val width: Float,
    val height: Float,
) {
    var uStart: Float = 0f
    var vStart: Float = 0f
    var uEnd: Float = 1f
    var vEnd: Float = 1f
}