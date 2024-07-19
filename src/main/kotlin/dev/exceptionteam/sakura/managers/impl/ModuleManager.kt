package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.impl.client.ChatInfo
import dev.exceptionteam.sakura.managers.AbstractManager
import java.util.concurrent.CopyOnWriteArrayList

object ModuleManager: AbstractManager() {

    val modules = CopyOnWriteArrayList<AbstractModule>()

    override fun onInit() {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            modules.forEach {
                if (it.keyBind == event.keyBind && event.action == 1) it.toggle()
            }
        }

        loadModules()
    }

    private fun loadModules() {
        loadClient()
    }

    private fun loadClient() {
        modules.add(ChatInfo)
    }

}