package dev.exceptionteam.sakura.mixins.core.packet;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundMovePlayerPacket.class)
public interface ServerboundMovePlayerPacketAccessor {

    @Accessor("onGround")
    @Mutable
    void setOnGround(boolean onGround);

    @Accessor("x")
    @Mutable
    void setX(double x);

    @Accessor("y")
    @Mutable
    void setY(double y);

    @Accessor("z")
    @Mutable
    void setZ(double z);

    @Accessor("xRot")
    @Mutable
    void setXRot(float yaw);

    @Accessor("yRot")
    @Mutable
    void setYRot(float pitch);

}
