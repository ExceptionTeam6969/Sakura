package dev.exceptionteam.sakura.utils.math.vector

class Vec3f(val x: Float, val y: Float, val z: Float) {
    constructor(x: Double, y: Double, z: Double)
        : this(x.toFloat(), y.toFloat(), z.toFloat())
}