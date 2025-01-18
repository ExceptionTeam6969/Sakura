package dev.exceptionteam.sakura.mixins.core.packet;


import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetEntityMotionPacket.class)
public interface ClientboundSetEntityMotionPacketAccessor {
    @Mutable
    @Accessor("xa")
    void setX(int xa);

    @Mutable
    @Accessor("ya")
    void setY(int ya);

    @Mutable
    @Accessor("za")
    void setZ(int za);
}
