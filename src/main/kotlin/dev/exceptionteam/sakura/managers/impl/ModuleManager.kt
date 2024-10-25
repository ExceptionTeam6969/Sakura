package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.features.modules.impl.client.*
import dev.exceptionteam.sakura.features.modules.impl.hud.WaterMark
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
            UiSetting,
            ClickGUI,
            HUDEditor,
            ChatInfo,
            CustomFont,
            RenderSystemMod,
            Language,

            // Movement
            Sprint,

            // HUD
            WaterMark,
        ).sortedBy { it.name.key }.toTypedArray()
    }


}