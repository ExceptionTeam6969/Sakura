package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.features.modules.impl.client.Language
import dev.exceptionteam.sakura.translation.TranslationKey
import dev.exceptionteam.sakura.translation.TranslationMap

object TranslationManager {

    const val DEFAULT_LANGUAGE = "en_us"

    val en = TranslationMap("en_us")
    val cn = TranslationMap("zh_cn")

    val defaultMap get() = en

    fun getTranslation(key: String): String {
        return when (Language.language) {
            Language.Languages.EN_US -> en[TranslationKey(key)] ?: key
            Language.Languages.ZH_CN -> cn[TranslationKey(key)] ?: key
            else -> defaultMap[TranslationKey(key)] ?: key
        }
    }

    fun getTranslation(key: TranslationKey): String {
        return when (Language.language) {
            Language.Languages.EN_US -> en[key] ?: key.key
            Language.Languages.ZH_CN -> cn[key] ?: key.key
            else -> defaultMap[key] ?: key.key
        }
    }

    fun getMapFromLanguage(language: String): TranslationMap =
        when (language) {
            "en_us" -> en
            "zh_cn" -> cn
            else -> en
        }

}