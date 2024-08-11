package dev.exceptionteam.sakura.utils.math.vector

class Vec2f(val x: Float, val y: Float) {

    constructor(x: Double, y: Double): this(x.toFloat(), y.toFloat())

}