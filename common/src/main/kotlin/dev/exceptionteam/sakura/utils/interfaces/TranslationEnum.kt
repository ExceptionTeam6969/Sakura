package dev.exceptionteam.sakura.utils.interfaces

import dev.exceptionteam.sakura.managers.impl.TranslationManager

interface TranslationEnum {

    val key: CharSequence

    val keyString: String
        get() = key.toString()

    val translation: String get() =
        TranslationManager.getTranslation(keyString)

}

interface DirectTranslationEnum {

    val key: CharSequence

    val keyString: String
        get() = key.toString()

    val translation: String get() =
        TranslationManager.getTranslation(keyString)

}