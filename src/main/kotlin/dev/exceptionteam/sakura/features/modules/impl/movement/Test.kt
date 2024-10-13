package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

object Test: Module(
    name = "test",
    category = Category.MOVEMENT
) {

    private val slider by setting("slider", 0.5, 0.0..1.0)
    private val mode by setting("mode", Mode.NORMAL)
    private val color by setting("color", ColorRGB(255, 0, 0))

    private enum class Mode(override val key: CharSequence): TranslationEnum {
        NORMAL("normal"),
        FUCK("fuck"),
    }

}
