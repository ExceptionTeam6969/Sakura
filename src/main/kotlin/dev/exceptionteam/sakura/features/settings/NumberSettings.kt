package dev.exceptionteam.sakura.features.settings

import dev.exceptionteam.sakura.translation.TranslationString

abstract class NumberSetting<N>(
    name: TranslationString,
    value: N,
    val minValue: N,
    val maxValue: N,
    val step: N,
    visibility: () -> Boolean
) : AbstractSetting<N>(name, value, visibility) where N: Number

class IntSetting(
    name: TranslationString,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    visibility: () -> Boolean
) : NumberSetting<Int>(name, value, minValue, maxValue, step, visibility)

class LongSetting(
    name: TranslationString,
    value: Long,
    minValue: Long,
    maxValue: Long,
    step: Long,
    visibility: () -> Boolean
) : NumberSetting<Long>(name, value, minValue, maxValue, step, visibility)

class FloatSetting(
    name: TranslationString,
    value: Float,
    minValue: Float,
    maxValue: Float,
    step: Float,
    visibility: () -> Boolean
) : NumberSetting<Float>(name, value, minValue, maxValue, step, visibility)

class DoubleSetting(
    name: TranslationString,
    value: Double,
    minValue: Double,
    maxValue: Double,
    step: Double,
    visibility: () -> Boolean
) : NumberSetting<Double>(name, value, minValue, maxValue, step, visibility)