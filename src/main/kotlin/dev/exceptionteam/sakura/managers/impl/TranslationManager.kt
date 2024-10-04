package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.managers.AbstractManager
import dev.exceptionteam.sakura.translation.TranslationKey
import dev.exceptionteam.sakura.translation.TranslationMap

object TranslationManager: AbstractManager() {

    const val DEFAULT_LANGUAGE = "en_us"

    val en = TranslationMap("en_us")
    val cn = TranslationMap("zh_cn")

    val defaultMap get() = en

    fun getTranslation(key: String): String =
        defaultMap[TranslationKey(key)] ?: "No translation"

    fun getTranslation(key: TranslationKey): String = defaultMap[key] ?: "No translation"

    fun getMapFromLanguage(language: String): TranslationMap =
        when (language) {
            "en_us" -> en
            "zh_cn" -> cn
            else -> en
        }

}