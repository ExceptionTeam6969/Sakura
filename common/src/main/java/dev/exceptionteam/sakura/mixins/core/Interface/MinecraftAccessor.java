package dev.exceptionteam.sakura.mixins.core.Interface;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor("fps")
    static int getFps() {
        return 0;
    }

}
