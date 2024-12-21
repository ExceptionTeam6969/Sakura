package dev.exceptionteam.sakura.addons

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.translation.TranslationKey

abstract class SakuraAddon {

    /**
     * Returns the language data of the addon.
     */
    abstract fun getLanguageData(): Map<String, Map<TranslationKey, String>>

    abstract fun onInitialize()

    abstract fun getModules(): List<AbstractModule>

}