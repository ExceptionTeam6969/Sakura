package dev.exceptionteam.sakura.features.modules

abstract class Module(
    name: String,
    category: Category,
    description: String,
    defaultEnable: Boolean = false,
    defaultBind: Int = -1
): AbstractModule(
    name,
    category,
    description,
    defaultEnable,
    defaultBind
)