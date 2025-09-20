package team.exception.sakura.modules

import com.mojang.blaze3d.platform.InputConstants
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.InputEvent
import team.exception.sakura.Sakura
import team.exception.sakura.modules.impl.render.*

@EventBusSubscriber(modid = Sakura.MOD_ID)
object ModuleManager {

    private val modules = arrayListOf<AbstractModule>()

    init {

        registerModules()

    }

    private fun registerModules() {
        modules.add(TestModule)
    }

    @SubscribeEvent
    fun onKeyPress(event: InputEvent.Key) {
        modules.forEach { module ->
            if (module.key.keyCode == event.key && event.action == InputConstants.PRESS) {
                module.toggle()
            }
        }
    }

}