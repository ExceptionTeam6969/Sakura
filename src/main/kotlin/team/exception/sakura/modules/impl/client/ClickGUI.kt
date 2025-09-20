package team.exception.sakura.modules.impl.client

import org.lwjgl.glfw.GLFW
import team.exception.sakura.gui.clickgui.ClickGUIScreen
import team.exception.sakura.modules.AbstractModule
import team.exception.sakura.modules.Category
import team.exception.sakura.utils.nonnull.runSafe

object ClickGUI: AbstractModule(
    name = "click-gui",
    category = Category.CLIENT,
    defaultKey = GLFW.GLFW_KEY_RIGHT_SHIFT
) {

    init {

        onEnable {
            runSafe {
                if (mc.screen == null) {
                    mc.setScreen(ClickGUIScreen)
                }
            }
        }

    }

}