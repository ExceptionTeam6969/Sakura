package team.exception.sakura.modules

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.InputEvent
import team.exception.sakura.Sakura
import team.exception.sakura.modules.impl.client.ClickGUI
import team.exception.sakura.modules.impl.render.*
import team.exception.sakura.utils.nonnull.runSafe

@EventBusSubscriber(modid = Sakura.MOD_ID)
object ModuleManager {

    private val modules = arrayListOf<AbstractModule>()

    init {

        registerModules()

    }

    private fun registerModules() {
        modules.addAll(listOf(
            TestModule,
            ClickGUI,
        ))
    }

    @SubscribeEvent
    fun onKeyPress(event: InputEvent.Key) = runSafe {
        if (mc.screen != null) return@runSafe
        modules.forEach { module ->
            if (module.key.keyCode == event.key && event.action == InputConstants.PRESS) {
                module.toggle()
            }
        }
    }

}