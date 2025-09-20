package team.exception.sakura

import com.mojang.logging.LogUtils
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = Sakura.MOD_ID, dist = [Dist.CLIENT])
object Sakura {

    const val MOD_ID = "sakura"

    val LOGGER: Logger = LogUtils.getLogger()

    init {

        MOD_BUS.addListener<FMLClientSetupEvent> {

            LOGGER.info("Sakura has been initialized.")
        }

    }

}