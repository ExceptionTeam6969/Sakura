package dev.exceptionteam.sakura.features.settings

import dev.exceptionteam.sakura.translation.TranslationString

abstract class NumberSetting<N>(
    name: TranslationString,
    value: N,
    val minValue: N,
    val maxValue: N,
    val step: N,
    description: CharSequence,
    visibility: () -> Boolean
) : AbstractSetting<N>(name, value, description, visibility) where N: Number

class IntSetting(
    name: TranslationString,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Int>(name, value, minValue, maxValue, step, description, visibility)

class LongSetting(
    name: TranslationString,
    value: Long,
    minValue: Long,
    maxValue: Long,
    step: Long,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Long>(name, value, minValue, maxValue, step, description, visibility)

class FloatSetting(
    name: TranslationString,
    value: Float,
    minValue: Float,
    maxValue: Float,
    step: Float,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Float>(name, value, minValue, maxValue, step, description, visibility)

class DoubleSetting(
    name: TranslationString,
    value: Double,
    minValue: Double,
    maxValue: Double,
    step: Double,
    description: CharSequence,
    visibility: () -> Boolean
) : NumberSetting<Double>(name, value, minValue, maxValue, step, description, visibility)