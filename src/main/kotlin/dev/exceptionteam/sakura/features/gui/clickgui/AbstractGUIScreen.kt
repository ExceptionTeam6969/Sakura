package dev.exceptionteam.sakura.features.gui.clickgui

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

abstract class AbstractGUIScreen(
    val name: CharSequence
): Screen(Text.literal("Sakura-$name")) {

    companion object {
        const val WIDTH = 160f
        const val HEIGHT = 20f
    }

    override fun shouldCloseOnEsc(): Boolean = false

}