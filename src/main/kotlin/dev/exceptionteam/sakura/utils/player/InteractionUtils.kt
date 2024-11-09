package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.builders.BlockHitResultBuilder
import dev.exceptionteam.sakura.utils.world.BlockUtils.getNeighbourSide
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

object InteractionUtils {

    fun NonNullContext.placeBlock(pos: BlockPos, hand: Hand = Hand.MAIN_HAND) {
        val side = getNeighbourSide(pos)

        val blockHit = BlockHitResultBuilder()
            .pos(pos.toCenterPos())
            .blockPos(pos)
            .side(side)
            .build()

        connection.sendPacket(PlayerInteractBlockC2SPacket(
            hand, blockHit, 0
        ))
    }

}