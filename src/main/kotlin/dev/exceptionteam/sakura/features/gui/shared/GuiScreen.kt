package dev.exceptionteam.sakura.features.gui.shared

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

open class GuiScreen(
    name: String,
): Screen(Component.literal("sakura:$name")) {

    override fun shouldCloseOnEsc(): Boolean = true

    override fun isPauseScreen(): Boolean = false

    companion object {
        const val WIDTH = 140f
        const val HEIGHT = 18f
    }

}