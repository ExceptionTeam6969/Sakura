package dev.exceptionteam.sakura.features.settings

import com.google.gson.JsonElement
import dev.exceptionteam.sakura.utils.graphics.color.ColorRGB
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractSetting<T>(
    var name: CharSequence,
    var value: T,
    var description: CharSequence,
    var visibility: () -> Boolean
) : ReadWriteProperty<Any, T> {

    private val defaultValue = value

    private val changeValueConsumers = CopyOnWriteArrayList<() -> Unit>()

    val settingId: String
        get() = "$name@${this::class.simpleName}"

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        value(value)
    }

    fun onChangeValue(run: () -> Unit): AbstractSetting<*> {
        return this.apply { changeValueConsumers.add(run) }
    }

    fun default() {
        value = defaultValue
    }

    //Builder
    fun name(name: CharSequence): AbstractSetting<T> {
        this.name = name
        return this
    }

    fun value(value: T): AbstractSetting<T> {
        this.value = value
        changeValueConsumers.forEach { it.invoke() }
        return this
    }

    fun description(desc: CharSequence): AbstractSetting<T> {
        this.description = desc
        return this
    }

    fun visibility(visibility: () -> Boolean): AbstractSetting<T> {
        this.visibility = visibility
        return this
    }

    fun setWithJson(v: JsonElement) {
        when (this) {
            is BooleanSetting -> this.value(v.asBoolean)
            is TextSetting -> this.value(v.asString)
            is ColorSetting -> this.value(ColorRGB(v.asInt))
            is EnumSetting<*> -> this.setWithName(v.asString)
            is KeyBindSetting -> this.value.valueFromString(v.asString)
            is NumberSetting<*> -> {
                when (this) {
                    is IntSetting -> this.value(v.asInt)
                    is LongSetting -> this.value(v.asLong)
                    is FloatSetting -> this.value(v.asFloat)
                    is DoubleSetting -> this.value(v.asDouble)
                }
            }
        }
    }
}