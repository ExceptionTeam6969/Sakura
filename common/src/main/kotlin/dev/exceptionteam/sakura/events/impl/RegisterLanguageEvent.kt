package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import dev.exceptionteam.sakura.translation.TranslationKey
import java.util.concurrent.ConcurrentHashMap

class RegisterLanguageEvent(
    val language: String,
    val map: ConcurrentHashMap<TranslationKey, String>
): Event()