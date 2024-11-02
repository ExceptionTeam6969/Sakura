package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.threads.runSafe

object HUDEditor: Module(
    name = "hud-editor",
    category = Category.CLIENT,
) {

    init {

        onEnable {
            runSafe {
                if (mc.currentScreen == ClickGUIScreen) ClickGUI.disable()
                mc.setScreen(HUDEditorScreen)
            }
        }

        onDisable {
            runSafe {
                if (mc.currentScreen is HUDEditorScreen) mc.setScreen(null)
            }
        }

    }

}