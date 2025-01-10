package dev.exceptionteam.sakura

import dev.exceptionteam.sakura.managers.Managers
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Sakura {

    val logger: Logger = LogManager.getLogger("Sakura")

    const val NAME = "Sakura"
    const val VERSION = "1.0.3"
    const val DIRECTORY = "sakura"
    const val ASSETS_DIRECTORY = "/assets/sakura"

    fun init() {
        Managers
    }

}