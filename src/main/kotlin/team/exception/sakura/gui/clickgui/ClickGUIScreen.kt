package team.exception.sakura.gui.clickgui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import team.exception.sakura.modules.impl.client.ClickGUI

object ClickGUIScreen: Screen(Component.literal("Sakura-ClickGUI")) {

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun renderBackground(): Boolean = false
    override fun isPauseScreen(): Boolean = false
    override fun shouldCloseOnEsc(): Boolean = true
    override fun onClose() {
        ClickGUI.disable()
        super.onClose()
    }

}