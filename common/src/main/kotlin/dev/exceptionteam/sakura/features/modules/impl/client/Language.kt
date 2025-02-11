package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

object Language: Module(
    name = "language",
    category = Category.CLIENT,
    defaultEnable = true,
    alwaysEnable = true,
) {

    val language by setting("language", Languages.EN_US)

    enum class Languages(override val key: CharSequence): TranslationEnum {
        EN_US("en_us"),
        ZH_CN("zh_cn"),
    }

}