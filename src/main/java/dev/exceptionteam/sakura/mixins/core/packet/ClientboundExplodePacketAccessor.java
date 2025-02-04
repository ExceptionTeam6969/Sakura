package dev.exceptionteam.sakura.mixins.core.packet;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(ClientboundExplodePacket.class)
public interface ClientboundExplodePacketAccessor {
    @Accessor("knockbackX")
    float getKnockbackX();

    @Accessor("knockbackY")
    float getKnockbackY();

    @Accessor("knockbackZ")
    float getKnockbackZ();
}
