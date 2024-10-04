package dev.exceptionteam.sakura.features.settings

import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.translation.TranslationString
import org.lwjgl.glfw.GLFW

interface SettingsDesigner<T : Any> {

    fun T.setting(
        name: CharSequence,
        value: Boolean = true,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(BooleanSetting(TranslationString("", name.toString()), value, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: String,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(TextSetting(TranslationString("", name.toString()), value, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: ColorRGB,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(ColorSetting(TranslationString("", name.toString()), value, descriptions, visibility))

    fun <E> T.setting(
        name: CharSequence,
        value: E,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) where E : Enum<E> = setting(EnumSetting(TranslationString("", name.toString()), value, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: Int = GLFW.GLFW_KEY_UNKNOWN,
        type: KeyBind.Type = KeyBind.Type.KEYBOARD,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(KeyBindSetting(TranslationString("", name.toString()), KeyBind(type, value), descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: Int,
        range: IntRange,
        step: Int = 1,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(IntSetting(TranslationString("", name.toString()), value, range.first, range.last, step, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: Long,
        range: LongRange,
        step: Long = 1L,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(LongSetting(TranslationString("", name.toString()), value, range.first, range.last, step, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: Float,
        range: ClosedFloatingPointRange<Float>,
        step: Float = 0.1f,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(FloatSetting(TranslationString("", name.toString()), value, range.start, range.endInclusive, step, descriptions, visibility))

    fun T.setting(
        name: CharSequence,
        value: Double,
        range: ClosedFloatingPointRange<Double>,
        step: Double = 0.01,
        descriptions: CharSequence = "",
        visibility: () -> Boolean = { true }
    ) = setting(DoubleSetting(TranslationString("", name.toString()), value, range.start, range.endInclusive, step, descriptions, visibility))

    fun <S : AbstractSetting<*>> T.setting(setting: S): S
}