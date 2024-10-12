package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.DoubleSetting
import dev.exceptionteam.sakura.features.settings.FloatSetting
import dev.exceptionteam.sakura.features.settings.IntSetting
import dev.exceptionteam.sakura.features.settings.LongSetting
import dev.exceptionteam.sakura.features.settings.NumberSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class SliderComponent(
    width: Float, height: Float, private val setting: NumberSetting<*>
): AbstractComponent(0f, 0f, width, height) {

    override val visible: Boolean
        get() = setting.visibility.invoke()

    private var isSliding = false

    override fun render() {
        if (isSliding) {
            val startX = x
            val endX = x + width
            val value0 = (mouseX - startX) / (endX - startX) * (setting.maxValue.toFloat() - setting.minValue.toFloat()) + setting.minValue.toFloat()

            val value = value0.coerceIn(setting.minValue.toFloat(), setting.maxValue.toFloat())

            when (setting) {
                is IntSetting -> setting.value = value.toInt()
                is LongSetting -> setting.value = value.toLong()
                is FloatSetting -> setting.value = value
                is DoubleSetting -> setting.value = value.toDouble()
                else -> {}
            }
        }

        FontRenderers.drawString("${setting.key.translation}: ${setting.value}", x + 5f, y + 4f, ColorRGB.BLACK)

        val sliderLength =
            width *(setting.value.toFloat().coerceIn(setting.minValue.toFloat(), setting.maxValue.toFloat())
                    - setting.minValue.toFloat()) /
                    (setting.maxValue.toFloat() - setting.minValue.toFloat())

        RenderUtils2D.drawRectFilled(x, y + height - 2.5f, sliderLength, 2.5f , ColorRGB.BLACK)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            isSliding = true
            return true
        }
        return false
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            isSliding = false
            return true
        }
        return false
    }

}