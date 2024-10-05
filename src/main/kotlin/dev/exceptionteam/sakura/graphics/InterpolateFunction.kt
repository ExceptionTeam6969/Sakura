package dev.exceptionteam.sakura.graphics

fun interface InterpolateFunction {
    fun invoke(time: Long, prev: Float, current: Float): Float
}