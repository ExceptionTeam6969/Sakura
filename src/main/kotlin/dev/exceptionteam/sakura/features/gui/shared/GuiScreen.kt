package dev.exceptionteam.sakura.features.gui.shared

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

open class GuiScreen(
    name: String,
): Screen(Text.literal("sakura:$name")) {

    override fun shouldCloseOnEsc(): Boolean = true

    override fun shouldPause(): Boolean = false

    companion object {
        const val WIDTH = 140f
        const val HEIGHT = 18f
    }

}