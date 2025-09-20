package team.exception.sakura.i18n

object I18NManager {

    val languageMaps: Map<LanguageType, LanguageMap> = mapOf(
        LanguageType.EN_US to LanguageMap.loadByLanguage(LanguageType.EN_US),
        LanguageType.ZH_CN to LanguageMap.loadByLanguage(LanguageType.ZH_CN)
    )

    var currentLanguage: LanguageType = LanguageType.EN_US; private set

    val defaultLanguage = LanguageType.EN_US

    operator fun get(key: String): String = getTranslation(key)

    fun getTranslation(key: String): String {
        languageMaps[currentLanguage]?.let {
            return it[key] ?: languageMaps[defaultLanguage]?.get(key) ?: ""
        }
        return languageMaps[defaultLanguage]?.get(key) ?: ""
    }

}