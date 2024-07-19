package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.managers.AbstractManager

object ModuleManager: AbstractManager() {

    override fun onInit() {
        loadModules()
    }

    private fun loadModules() {
    }

}