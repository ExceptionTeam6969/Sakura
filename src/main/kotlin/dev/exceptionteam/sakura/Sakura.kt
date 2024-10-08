package dev.exceptionteam.sakura

import dev.exceptionteam.sakura.managers.Managers
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW

object Sakura {

    val logger = LogManager.getLogger("Sakura")

    const val NAME = "Sakura"
    const val VERSION = "1.0"
    const val DIRECTORY = "sakura"
    const val ASSETS_DIRECTORY = "/assets/sakura"

    @JvmStatic
    fun init() {
        Managers
    }

}