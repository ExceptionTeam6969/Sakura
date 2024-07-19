package dev.exceptionteam.sakura.features.modules

import dev.exceptionteam.sakura.utils.interfaces.DisplayEnum

enum class Category(override val displayName: CharSequence): DisplayEnum {

    COMBAT("Combat"),
    MISC("Misc"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    CLIENT("Client"),
    HUD("HUD")

}