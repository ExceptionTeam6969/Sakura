package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.impl.client.*
import dev.exceptionteam.sakura.features.modules.impl.hud.*
import dev.exceptionteam.sakura.features.modules.impl.misc.*
import dev.exceptionteam.sakura.features.modules.impl.movement.*
import dev.exceptionteam.sakura.features.modules.impl.render.*

object ModuleManager {

    lateinit var modules: Array<AbstractModule>

    init {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            if (event.isCancelled) return@nonNullListener
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

            // Misc
            FakePlayer,

            // Render
            NameTags,
            FullBright,

            // Movement
            Sprint,
            Velocity,

            // HUD
            WaterMark,
        ).sortedBy { it.name.key }.toTypedArray()
    }


}