package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.impl.client.*
import dev.exceptionteam.sakura.features.modules.impl.movement.*

object ModuleManager {

    lateinit var modules: Array<AbstractModule>

    init {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            modules.forEach {
                if (it.keyBind == event.keyBind && event.action == 1) it.toggle()
            }
        }

        loadModules()
    }

    private fun loadModules() {
        modules = arrayOf(
            // Client
            ClickGUI,
            ChatInfo,
            CustomFont,
            RenderSystemMod,
            Language,

            // Movement
            Sprint,
        )
    }


}