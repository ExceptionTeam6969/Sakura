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

    private val page by setting("page", Page.GENERAL)

    // General
    val background by setting("background", true) { page == Page.GENERAL }

    // Color
    val primaryColor by setting("primary-color", ColorRGB(255, 255, 255)) { page == Page.COLOR }
    val secondaryColor by setting("secondary-color", ColorRGB(200, 200, 200)) { page == Page.COLOR }
    val textColor by setting("text-color", ColorRGB(0, 0, 0)) { page == Page.COLOR }
    val outlineColor by setting("outline-color", ColorRGB(0, 0, 0)) { page == Page.COLOR }
    val sliderColor by setting("slider-color", ColorRGB(0, 0, 0)) { page == Page.COLOR }
    val backgroundColor by setting("background-color", ColorRGB(255, 255, 255)) { page == Page.COLOR }

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

    private enum class Page(override val key: CharSequence): TranslationEnum {
        GENERAL("general"),
        COLOR("color"),
    }

}