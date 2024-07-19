package dev.exceptionteam.sakura.features.settings

abstract class NumberSetting<N>(
    name: CharSequence,
    value: N,
    val minValue: N,
    val maxValue: N,
    val step: N,
    description: CharSequence,
    visibility: () -> Boolean
) : AbstractSetting<N>(name, value, description, visibility) where N: Number

class IntSetting(
    name: CharSequence,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Int>(name, value, minValue, maxValue, step, description, visibility)

class LongSetting(
    name: CharSequence,
    value: Long,
    minValue: Long,
    maxValue: Long,
    step: Long,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Long>(name, value, minValue, maxValue, step, description, visibility)

class FloatSetting(
    name: CharSequence,
    value: Float,
    minValue: Float,
    maxValue: Float,
    step: Float,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Float>(name, value, minValue, maxValue, step, description, visibility)

class DoubleSetting(
    name: CharSequence,
    value: Double,
    minValue: Double,
    maxValue: Double,
    step: Double,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Double>(name, value, minValue, maxValue, step, description, visibility)