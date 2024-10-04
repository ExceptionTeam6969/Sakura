package dev.exceptionteam.sakura.features.gui.shared

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

abstract class AbstractGUIScreen(
    val name: CharSequence
): Screen(Text.literal("Sakura-$name")) {

    companion object {
        const val WIDTH = 140f
        const val HEIGHT = 18f
    }

    override fun shouldCloseOnEsc(): Boolean = true

    override fun shouldPause(): Boolean = false

}