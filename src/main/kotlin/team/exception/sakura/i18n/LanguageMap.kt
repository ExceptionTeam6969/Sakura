package team.exception.sakura.i18n

import team.exception.sakura.utils.resources.Resource

class LanguageMap(
    private val map: MutableMap<String, String> = mutableMapOf()
) {

    operator fun get(key: String): String? {
        return map[key]
    }

    companion object {

        fun loadByLanguage(language: LanguageType): LanguageMap {
            val res = Resource("lang/${language.type}.lang").getDataAsString()
            val map = mutableMapOf<String, String>()
            res.lines().forEach { line ->
                val keyValue = line.split("=")
                if (keyValue.size == 2) {
                    map[keyValue[0]] = keyValue[1]
                }
            }
            return LanguageMap(map)
        }

    }

}