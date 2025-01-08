package dev.exceptionteam.sakura.mixins.packet;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(ClientboundExplodePacket.class)
public interface IExplosionS2CPacket {
    /**
     * 获取爆炸包中的 playerKnockback 字段。
     *
     * @return 玩家的击退力作为 Optional<Vec3>
     */
    @Accessor("playerKnockback")
    Optional<Vec3> getPlayerKnockback();
}
