package team.exception.sakura.settings

import org.lwjgl.glfw.GLFW
import team.exception.sakura.graphics.color.ColorRGB
import team.exception.sakura.utils.input.KeyBind

interface SettingsDesigner<T : Any> {

    fun T.setting(
        name: CharSequence,
        value: Boolean = true,
        visibility: () -> Boolean = { true }
    ) = setting(BooleanSetting(name.toString(), value, visibility))

    fun T.setting(
        name: CharSequence,
        value: String,
        visibility: () -> Boolean = { true }
    ) = setting(TextSetting(name.toString(), value, visibility))

    fun T.setting(
        name: CharSequence,
        value: ColorRGB,
        visibility: () -> Boolean = { true }
    ) = setting(ColorSetting(name.toString(), value, visibility))

    fun <E> T.setting(
        name: CharSequence,
        value: E,
        visibility: () -> Boolean = { true }
    ) where E : Enum<E> = setting(EnumSetting(name.toString(), value, visibility))

    fun T.setting(
        name: CharSequence,
        value: Int = GLFW.GLFW_KEY_UNKNOWN,
        type: KeyBind.Type = KeyBind.Type.KEYBOARD,
        visibility: () -> Boolean = { true }
    ) = setting(KeyBindSetting(name.toString(), KeyBind(type, value), visibility))

    fun T.setting(
        name: CharSequence,
        value: Int,
        range: IntRange,
        step: Int = 1,
        visibility: () -> Boolean = { true }
    ) = setting(IntSetting(name.toString(), value, range.first, range.last, step, visibility))

    fun T.setting(
        name: CharSequence,
        value: Long,
        range: LongRange,
        step: Long = 1L,
        visibility: () -> Boolean = { true }
    ) = setting(LongSetting(name.toString(), value, range.first, range.last, step, visibility))

    fun T.setting(
        name: CharSequence,
        value: Float,
        range: ClosedFloatingPointRange<Float>,
        step: Float = 0.1f,
        visibility: () -> Boolean = { true }
    ) = setting(FloatSetting(name.toString(), value, range.start, range.endInclusive, step, visibility))

    fun T.setting(
        name: CharSequence,
        value: Double,
        range: ClosedFloatingPointRange<Double>,
        step: Double = 0.01,
        visibility: () -> Boolean = { true }
    ) = setting(DoubleSetting(name.toString(), value, range.start, range.endInclusive, step, visibility))

    fun <S : AbstractSetting<*>> T.setting(setting: S): S
}