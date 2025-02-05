package dev.exceptionteam.sakura.features.modules

import dev.exceptionteam.sakura.translation.TranslationString

abstract class Module(
    name: String,
    category: Category,
    defaultEnable: Boolean = false,
    alwaysEnable: Boolean = false,
    defaultBind: Int = -1
): AbstractModule(
    TranslationString("modules", name),
    category,
    TranslationString("modules", "description"),
    defaultEnable,
    alwaysEnable,
    defaultBind
)