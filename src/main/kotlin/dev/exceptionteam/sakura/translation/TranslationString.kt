package dev.exceptionteam.sakura.translation

import dev.exceptionteam.sakura.managers.impl.TranslationManager

data class TranslationString(
    var prefix: String,
    val key: String
) {
    val fullKey: String = "$prefix.$key"

    val translation: String get() = TranslationManager.getTranslation(fullKey)
}