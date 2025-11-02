package team.exception.sakura

import com.mojang.logging.LogUtils
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import org.slf4j.Logger
import team.exception.sakura.events.ToggleModuleEvent
import team.exception.sakura.managers.Managers
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = Sakura.MOD_ID, dist = [Dist.CLIENT])
@EventBusSubscriber(modid = Sakura.MOD_ID)
object Sakura {

    const val MOD_ID = "sakura"

    val LOGGER: Logger = LogUtils.getLogger()

    init {

        MOD_BUS.addListener<FMLClientSetupEvent> {
            Managers
            LOGGER.info("Sakura has been initialized.")
        }

    }

    @SubscribeEvent
    fun onModuleToggle(event: ToggleModuleEvent) {
        LOGGER.info("Toggled module ${event.module.name}" + if (event.enabled) " ON" else " OFF")
    }

    @SubscribeEvent
    fun onRenderSystemStart(event: )

}