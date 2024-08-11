package dev.exceptionteam.sakura.features.settings

import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import java.util.concurrent.CopyOnWriteArrayList

class BooleanSetting @JvmOverloads constructor(
    name: CharSequence = "",
    value: Boolean = false,
    description: CharSequence = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<Boolean>(name, value, description, visibility)

class TextSetting @JvmOverloads constructor(
    name: CharSequence = "",
    value: String = "",
    description: CharSequence = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<String>(name, value, description, visibility)

class ColorSetting @JvmOverloads constructor(
    name: CharSequence = "",
    value: ColorRGB = ColorRGB.WHITE,
    description: CharSequence = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<ColorRGB>(name, value, description, visibility)

class EnumSetting<E> @JvmOverloads constructor(
    name: CharSequence = "",
    value: E,
    description: CharSequence = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<E>(name, value, description, visibility) where E : Enum<E> {

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
    name: CharSequence = "",
    value: KeyBind = KeyBind(KeyBind.Type.KEYBOARD, -1, 1),
    description: CharSequence = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<KeyBind>(name, value, description, visibility) {
    private val pressConsumer = CopyOnWriteArrayList<() -> Unit>()

    fun onPress(run: () -> Unit) = pressConsumer.add(run)
}