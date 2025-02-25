package dev.exceptionteam.sakura.mixins.core.Interface;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface ILocalPlayer {

    @Accessor(value = "xRotLast")
    float getLastYaw();

    @Accessor(value = "yRotLast")
    float getLastPitch();

}
