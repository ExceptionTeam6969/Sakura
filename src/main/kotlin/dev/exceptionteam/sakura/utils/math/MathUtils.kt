package dev.exceptionteam.sakura.utils.math

import kotlin.math.*

object MathUtils {
    @JvmStatic
    fun ceilToPOT(valueIn: Int): Int {
        // Magical bit shifting
        var i = valueIn
        i--
        i = i or (i shr 1)
        i = i or (i shr 2)
        i = i or (i shr 4)
        i = i or (i shr 8)
        i = i or (i shr 16)
        i++
        return i
    }

    @JvmStatic
    fun round(value: Float, places: Int): Float {
        val scale = 10.0f.pow(places)
        return round(value * scale) / scale
    }

    @JvmStatic
    fun round(value: Double, places: Int): Double {
        val scale = 10.0.pow(places)
        return round(value * scale) / scale
    }

    @JvmStatic
    fun decimalPlaces(value: Double) = value.toString().split('.').getOrElse(1) { "0" }.length

    @JvmStatic
    fun decimalPlaces(value: Float) = value.toString().split('.').getOrElse(1) { "0" }.length

    @JvmStatic
    fun isNumberEven(i: Int): Boolean {
        return i and 1 == 0
    }

    @JvmStatic
    fun reverseNumber(num: Int, min: Int, max: Int): Int {
        return max + min - num
    }

    @JvmStatic
    fun convertRange(valueIn: Int, minIn: Int, maxIn: Int, minOut: Int, maxOut: Int): Int {
        return convertRange(
            valueIn.toDouble(),
            minIn.toDouble(),
            maxIn.toDouble(),
            minOut.toDouble(),
            maxOut.toDouble()
        ).toInt()
    }

    @JvmStatic
    fun convertRange(valueIn: Float, minIn: Float, maxIn: Float, minOut: Float, maxOut: Float): Float {
        return convertRange(
            valueIn.toDouble(),
            minIn.toDouble(),
            maxIn.toDouble(),
            minOut.toDouble(),
            maxOut.toDouble()
        ).toFloat()
    }

    @JvmStatic
    fun convertRange(valueIn: Double, minIn: Double, maxIn: Double, minOut: Double, maxOut: Double): Double {
        val rangeIn = maxIn - minIn
        val rangeOut = maxOut - minOut
        val convertedIn = (valueIn - minIn) * (rangeOut / rangeIn) + minOut
        val actualMin = min(minOut, maxOut)
        val actualMax = max(minOut, maxOut)
        return min(max(convertedIn, actualMin), actualMax)
    }

    @JvmStatic
    fun lerp(from: Double, to: Double, delta: Double): Double {
        return from + (to - from) * delta
    }

    @JvmStatic
    fun lerp(from: Double, to: Double, delta: Float): Double {
        return from + (to - from) * delta
    }

    @JvmStatic
    fun lerp(from: Float, to: Float, delta: Double): Float {
        return from + (to - from) * delta.toFloat()
    }

    @JvmStatic
    fun lerp(from: Float, to: Float, delta: Float): Float {
        return from + (to - from) * delta
    }

    @JvmStatic
    fun clamp(value: Double, min: Double, max: Double): Double {
        return value.coerceIn(min, max)
    }

    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float): Float {
        return value.coerceIn(min, max)
    }

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return value.coerceIn(min, max)
    }

    @JvmStatic
    fun approxEq(a: Double, b: Double, epsilon: Double = 0.0001): Boolean {
        return abs(a - b) < epsilon
    }

    @JvmStatic
    fun approxEq(a: Float, b: Float, epsilon: Float = 0.0001f): Boolean {
        return abs(a - b) < epsilon
    }

    @JvmStatic
    fun frac(value: Double): Double {
        return value - floor(value)
    }

    @JvmStatic
    fun frac(value: Float): Float {
        return value - floor(value)
    }

    fun roundToDecimal(n: Double, point: Int): Double {
        if (point == 0) {
            return floor(n)
        }
        val factor = 10.0.pow(point.toDouble())
        return round(n * factor) / factor
    }
}

const val PI_FLOAT = 3.14159265358979323846f

fun Double.floorToInt(): Int {
    return floor(this).toInt()
}

fun Double.ceilToInt(): Int {
    return ceil(this).toInt()
}

fun Float.floorToInt(): Int {
    val offset = -(this - 1.0f).toInt()
    return (this + offset).toInt() - offset
}

fun Float.ceilToInt(): Int {
    val offset = (this + 1.0f).toInt()
    return offset - (offset - this).toInt()
}

fun Float.toRadians() = this / 180.0f * PI_FLOAT
fun Double.toRadians() = this / 180.0 * PI

fun Float.toDegree() = this * 180.0f / PI_FLOAT
fun Double.toDegree() = this * 180.0 / PI

val Double.sq: Double get() = this * this
val Float.sq: Float get() = this * this
val Int.sq: Int get() = this * this

val Double.cubic: Double get() = this * this * this
val Float.cubic: Float get() = this * this * this
val Int.cubic: Int get() = this * this * this

val Double.quart: Double get() = this * this * this * this
val Float.quart: Float get() = this * this * this * this
val Int.quart: Int get() = this * this * this * this

val Double.quint: Double get() = this * this * this * this * this
val Float.quint: Float get() = this * this * this * this * this
val Int.quint: Int get() = this * this * this * this * this

val Int.isEven: Boolean get() = this and 1 == 0
val Int.isOdd: Boolean get() = this and 1 == 1