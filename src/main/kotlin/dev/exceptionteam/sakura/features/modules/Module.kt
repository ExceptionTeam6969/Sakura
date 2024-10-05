package dev.exceptionteam.sakura.features.modules

import dev.exceptionteam.sakura.translation.TranslationString

abstract class Module(
    name: String,
    category: Category,
    description: String,
    defaultEnable: Boolean = false,
    defaultBind: Int = -1
): AbstractModule(
    TranslationString("modules", name),
    category,
    description,
    defaultEnable,
    defaultBind
)