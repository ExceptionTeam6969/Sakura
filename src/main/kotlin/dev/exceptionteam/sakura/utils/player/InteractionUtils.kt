package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.builders.BlockHitResultBuilder
import dev.exceptionteam.sakura.utils.math.toVec3Center
import dev.exceptionteam.sakura.utils.world.BlockUtils.getNeighbourSide
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.InteractionHand

object InteractionUtils {

    fun NonNullContext.placeBlock(pos: BlockPos, hand: InteractionHand = InteractionHand.MAIN_HAND) {
        val side = getNeighbourSide(pos)

        val blockHit = BlockHitResultBuilder()
            .pos(pos.toVec3Center())
            .blockPos(pos)
            .side(side)
            .build()

        connection.send(ServerboundUseItemOnPacket(
            hand, blockHit, 0
        ))
    }

}