package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.threads.runSafe

object ClickGUI: Module(
    name = "ClickGUI",
    description = "",
    category = Category.CLIENT
) {

    init {

        onEnable {
            runSafe {
                mc.setScreen(ClickGUIScreen)
            }
        }

        onDisable {
            runSafe {
                mc.setScreen(null)
            }
        }

    }

}