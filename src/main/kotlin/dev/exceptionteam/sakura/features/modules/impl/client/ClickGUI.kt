package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.threads.runSafe
import org.lwjgl.glfw.GLFW

object ClickGUI: Module(
    name = "click-gui",
    category = Category.CLIENT,
    defaultBind = GLFW.GLFW_KEY_RIGHT_SHIFT,
) {

    init {

        onEnable {
            runSafe {
                mc.setScreen(ClickGUIScreen)
            }
        }

        onDisable {
            runSafe {
                if (mc.currentScreen is ClickGUIScreen) mc.setScreen(null)
            }
        }

    }

}