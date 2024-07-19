package dev.exceptionteam.sakura

import dev.exceptionteam.sakura.managers.Managers

object Sakura {

    const val NAME = "Sakura"
    const val VERSION = "1.0"

    @JvmStatic
    fun init() {
        Managers.init()
    }

}