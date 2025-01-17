package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

object CustomFont: Module(
    name = "custom-font",
    category = Category.CLIENT,
    defaultEnable = true,
    alwaysEnable = true
) {

    val fontSize by setting("font-size", 12, 1..40)
    val fontMode by setting("font-mode", FontMode.GENERAL)

    enum class FontMode(override val key: CharSequence): TranslationEnum {
        GENERAL("general"),
        SPARSE("sparse"),
    }

}