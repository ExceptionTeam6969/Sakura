package dev.exceptionteam.sakura.features.modules

import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

enum class Category(override val key: CharSequence): TranslationEnum {

    COMBAT("category.combat"),
    MISC("category.misc"),
    MOVEMENT("category.movement"),
    PLAYER("category.player"),
    RENDER("category.render"),
    CLIENT("category.client"),
    HUD("category.hud")

}