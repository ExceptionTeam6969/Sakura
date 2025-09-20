package team.exception.sakura.settings

import team.exception.sakura.graphics.color.ColorRGB
import team.exception.sakura.utils.input.KeyBind
import java.util.concurrent.CopyOnWriteArrayList

class BooleanSetting @JvmOverloads constructor(
    name: String,
    value: Boolean = false,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Boolean>(name, value, visibility)

class TextSetting @JvmOverloads constructor(
    name: String,
    value: String = "",
    visibility: () -> Boolean = { true }
) : AbstractSetting<String>(name, value, visibility)

class ColorSetting @JvmOverloads constructor(
    name: String,
    value: ColorRGB = ColorRGB.WHITE,
    visibility: () -> Boolean = { true }
) : AbstractSetting<ColorRGB>(name, value, visibility)

class EnumSetting<E> @JvmOverloads constructor(
    name: String,
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
    name: String,
    value: KeyBind = KeyBind(),
    visibility: () -> Boolean = { true }
) : AbstractSetting<KeyBind>(name, value, visibility) {
    private val pressConsumer = CopyOnWriteArrayList<() -> Unit>()

    fun onPress(run: () -> Unit) = pressConsumer.add(run)
}