package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.impl.RegisterModuleEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.impl.client.*
import dev.exceptionteam.sakura.features.modules.impl.combat.*
import dev.exceptionteam.sakura.features.modules.impl.hud.*
import dev.exceptionteam.sakura.features.modules.impl.misc.*
import dev.exceptionteam.sakura.features.modules.impl.movement.*
import dev.exceptionteam.sakura.features.modules.impl.player.*
import dev.exceptionteam.sakura.features.modules.impl.render.*

object ModuleManager {

    lateinit var modules: Array<AbstractModule>

    init {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            if (event.isCancelled) return@nonNullListener

            // Only in the game
            if (mc.screen != null) return@nonNullListener

            modules.forEach {
                if (it.keyBind == event.keyBind && event.action == 1) it.toggle()
            }
        }

        loadModules()
    }

    private fun loadModules() {
        modules = arrayOf(
            // Combat
            AutoBottle,
            AutoCrystal,
            FeetTrap,
            HolePush,
            KillAura,
            AutoTotem,
            PearlClip,

            // Misc
            PacketEat,
            Disabler,
            FakePlayer,

            // Render
            NameTags,
            FullBright,
            GameAnimation,
            NoRender,

            // Movement
            Sprint,
            Velocity,
            StrafeFix,

            // Player
            NoFall,
            CancelUsing,
            PacketDigging,

            // Client
            UiSetting,
            ClickGUI,
            HUDEditor,
            ChatInfo,
            CombatSettings,
            CustomFont,
            RenderSystemMod,
            Language,
            Rotations,

            // HUD
            WaterMark,
            GuiImage,
            Welcomer,
            FPS,
        )

        val event = RegisterModuleEvent(modules.toMutableList())
        event.post()
        modules = event.modules.sortedBy { it.name.key }.toTypedArray()
    }


}