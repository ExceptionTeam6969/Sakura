package dev.exceptionteam.sakura.features.settings

import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.translation.TranslationString
import java.util.concurrent.CopyOnWriteArrayList

class BooleanSetting @JvmOverloads constructor(
    name: TranslationString,
    value: Boolean = false,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Boolean>(name, value, visibility)

class TextSetting @JvmOverloads constructor(
    name: TranslationString,
    value: String = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<String>(name, value, visibility)

class ColorSetting @JvmOverloads constructor(
    name: TranslationString,
    value: ColorRGB = ColorRGB.WHITE,
    visibility: () -> Boolean = { true }
) : AbstractSetting<ColorRGB>(name, value, visibility)

class EnumSetting<E> @JvmOverloads constructor(
    name: TranslationString,
    value: E,
    visibility: () -> Boolean = { true }
) : AbstractSetting<E>(name, value, visibility) where E : Enum<E> {

    @Throws(NoSuchFieldException::class)
    fun forwardLoop() {
        this.value = this.value::class.java.enumConstants[(value.ordinal + 1) % value::class.java.enumConstants.size]
    }

    fun setWithName(name: String) {
        value::class.java.enumConstants.forEach {
            if (it.name == name) value = it
        }
    }
}

class KeyBindSetting @JvmOverloads constructor(
    name: TranslationString,
    value: KeyBind = KeyBind(KeyBind.Type.KEYBOARD, -1, 1),
    visibility: () -> Boolean = { true }
) : AbstractSetting<KeyBind>(name, value, visibility) {
    private val pressConsumer = CopyOnWriteArrayList<() -> Unit>()

    fun onPress(run: () -> Unit) = pressConsumer.add(run)
}