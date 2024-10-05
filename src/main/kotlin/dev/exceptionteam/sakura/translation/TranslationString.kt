package dev.exceptionteam.sakura.translation

import dev.exceptionteam.sakura.managers.impl.TranslationManager

data class TranslationString(
    var prefix: String,
    val key: String
) {
    var fullKey: String = "$prefix.$key" ;private set

    val translation: String get() = TranslationManager.getTranslation(fullKey)

    fun updateKey() {
        fullKey = "$prefix.$key"
    }
}