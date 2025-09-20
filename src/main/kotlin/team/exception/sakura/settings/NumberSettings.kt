package team.exception.sakura.settings

abstract class NumberSetting<N>(
    name: String,
    value: N,
    val minValue: N,
    val maxValue: N,
    val step: N,
    visibility: () -> Boolean
) : AbstractSetting<N>(name, value, visibility) where N: Number

class IntSetting(
    name: String,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    visibility: () -> Boolean
) : NumberSetting<Int>(name, value, minValue, maxValue, step, visibility)

class LongSetting(
    name: String,
    value: Long,
    minValue: Long,
    maxValue: Long,
    step: Long,
    visibility: () -> Boolean
) : NumberSetting<Long>(name, value, minValue, maxValue, step, visibility)

class FloatSetting(
    name: String,
    value: Float,
    minValue: Float,
    maxValue: Float,
    step: Float,
    visibility: () -> Boolean
) : NumberSetting<Float>(name, value, minValue, maxValue, step, visibility)

class DoubleSetting(
    name: String,
    value: Double,
    minValue: Double,
    maxValue: Double,
    step: Double,
    visibility: () -> Boolean
) : NumberSetting<Double>(name, value, minValue, maxValue, step, visibility)