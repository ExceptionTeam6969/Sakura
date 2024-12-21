package dev.exceptionteam.sakura.translation

import dev.exceptionteam.sakura.events.impl.RegisterLanguageEvent
import dev.exceptionteam.sakura.managers.impl.TranslationManager
import dev.exceptionteam.sakura.utils.resources.Resource
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.get

class TranslationMap(
    val language: String
) {
    private var translations: ConcurrentHashMap<TranslationKey, String> = ConcurrentHashMap()
    private val resource = Resource("lang/$language.lang")

    init {
        // Load translations from file
        resource.data.lines().forEach { line ->
            if (!line.startsWith("#")) {
                val split = line.split("=")
                if (split.size == 2) translations[TranslationKey(split[0])] = split[1]
            }
        }

        val event = RegisterLanguageEvent(language, translations)
        event.post()
        translations = event.map
    }

    operator fun get(key: TranslationKey): String? {
        val translation = translations[key]
        if (translation == null) {
            if (this == TranslationManager.defaultMap) return null
            return TranslationManager.defaultMap[key]
        }
        return translation
    }

    override fun equals(other: Any?): Boolean {
        return if (other is TranslationMap) {
            other.language == language
        } else false
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + translations.hashCode()
        result = 31 * result + resource.hashCode()
        return result
    }

}