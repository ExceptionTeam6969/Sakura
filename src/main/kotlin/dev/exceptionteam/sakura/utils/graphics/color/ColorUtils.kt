package dev.exceptionteam.sakura.utils.graphics.color

import java.awt.Color
import kotlin.math.ceil

@Suppress("NOTHING_TO_INLINE")
object ColorUtils {

    const val ONE_THIRD = 1.0f / 3.0f
    const val TWO_THIRD = 2.0f / 3.0f

    inline fun rainbowHSB(delay: Int, saturation: Float, brightness: Float): ColorHSB {
        var rainbowState = ceil((System.currentTimeMillis() + delay) / 20.0)
        rainbowState %= 360.0
        return ColorHSB((rainbowState / 360.0f).toFloat(), saturation, brightness)
    }

    inline fun rainbowRGB(delay: Int, s: Float, b: Float): ColorRGB = rainbowHSB(delay, s, b).toRGB()


    inline fun getRed(hex: Int): Int {
        return hex shr 16 and 255
    }

    inline fun getGreen(hex: Int): Int {
        return hex shr 8 and 255
    }

    inline fun getBlue(hex: Int): Int {
        return hex and 255
    }

    inline fun getHoovered(color: Int, isHoovered: Boolean): ColorRGB {
        return if (isHoovered) ColorRGB(color and 0x7F7F7F shl 1) else ColorRGB(color)
    }

    inline fun getHoovered(color: Color, isHoovered: Boolean): Int {
        return if (isHoovered) color.rgb and 0x7F7F7F shl 1 else color.rgb
    }


    inline fun argbToRgba(argb: Int) =
        (argb and 0xFFFFFF shl 8) or
                (argb shr 24 and 255)

    inline fun rgbaToArgb(rgba: Int) =
        (rgba shr 8 and 0xFFFFFF) or
                (rgba and 255 shl 24)

    inline fun rgbToHSB(r: Int, g: Int, b: Int, a: Int): ColorHSB {
        val cMax = maxOf(r, g, b)
        if (cMax == 0) return ColorHSB(0.0f, 0.0f, 0.0f, a / 255.0f)

        val cMin = minOf(r, g, b)
        val diff = cMax - cMin

        val diff6 = diff * 6.0f

        var hue = when (cMax) {
            cMin -> {
                0.0f
            }

            r -> {
                (g - b) / diff6 + 1.0f
            }

            g -> {
                (b - r) / diff6 + ONE_THIRD
            }

            else -> {
                (r - g) / diff6 + TWO_THIRD
            }
        }

        hue %= 1.0f

        val saturation = diff / cMax.toFloat()
        val brightness = cMax / 255.0f

        return ColorHSB(hue, saturation, brightness, a / 255.0f)
    }

    inline fun rgbToHue(r: Int, g: Int, b: Int): Float {
        val cMax = maxOf(r, g, b)
        if (cMax == 0) return 0.0f

        val cMin = minOf(r, g, b)
        if (cMax == cMin) return 0.0f

        val diff = (cMax - cMin) * 6.0f

        val hue = when (cMax) {
            r -> {
                (g - b) / diff + 1.0f
            }

            g -> {
                (b - r) / diff + ONE_THIRD
            }

            else -> {
                (r - g) / diff + TWO_THIRD
            }
        }

        return hue % 1.0f
    }

    inline fun rgbToSaturation(r: Int, g: Int, b: Int): Float {
        val cMax = maxOf(r, g, b)
        if (cMax == 0) return 0.0f

        val cMin = minOf(r, g, b)
        val diff = cMax - cMin

        return diff / cMax.toFloat()
    }

    inline fun rgbToBrightness(r: Int, g: Int, b: Int): Float {
        return maxOf(r, g, b) / 255.0f
    }

    inline fun rgbToLightness(r: Int, g: Int, b: Int): Float {
        return (maxOf(r, g, b) + minOf(r, g, b)) / 510.0f
    }

    inline fun hsbToRGB(h: Float, s: Float, b: Float): ColorRGB {
        return hsbToRGB(h, s, b, 1.0f)
    }

    inline fun hsbToRGB(h: Float, s: Float, b: Float, a: Float): ColorRGB {
        val hue6 = (h % 1.0f) * 6.0f
        val intHue6 = hue6.toInt()
        val f = hue6 - intHue6
        val p = b * (1.0f - s)
        val q = b * (1.0f - f * s)
        val t = b * (1.0f - (1.0f - f) * s)

        return when (intHue6) {
            0 -> ColorRGB(b, t, p, a)
            1 -> ColorRGB(q, b, p, a)
            2 -> ColorRGB(p, b, t, a)
            3 -> ColorRGB(p, q, b, a)
            4 -> ColorRGB(t, p, b, a)
            5 -> ColorRGB(b, p, q, a)
            else -> ColorRGB(255, 255, 255)
        }
    }

    inline fun Int.toColor(): ColorRGB {
        return ColorRGB(this)
    }

    inline fun Color.toRGB() = ColorRGB(argbToRgba(this.rgb))

    inline fun Color.toHSB() = ColorRGB(this.red, this.green, this.blue, this.alpha).toHSB()
}