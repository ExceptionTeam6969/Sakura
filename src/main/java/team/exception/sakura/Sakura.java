package team.exception.sakura;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@Mod(value = Sakura.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Sakura.MODID, value = Dist.CLIENT)
public class Sakura {
    public static final String MODID = "sakura";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Sakura(ModContainer modContainer) {
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Sakura.LOGGER.info("HELLO FROM CLIENT SETUP");
        Sakura.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

}
