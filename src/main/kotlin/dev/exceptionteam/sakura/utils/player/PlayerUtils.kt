package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.math.toBlockPos
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box

object PlayerUtils {

    fun NonNullContext.isMoving() =
        player.sidewaysSpeed != 0f || player.upwardSpeed != 0f || player.forwardSpeed != 0f

    fun NonNullContext.isInBlock(): Boolean =
        isIntersectingBlock(player.blockPos)
            || isIntersectingBlock(player.pos.add(0.3, 0.0, 0.3).toBlockPos())
            || isIntersectingBlock(player.pos.add(-0.3, 0.0, 0.3).toBlockPos())
            || isIntersectingBlock(player.pos.add(-0.3, 0.0, -0.3).toBlockPos())
            || isIntersectingBlock(player.pos.add(0.3, 0.0, -0.3).toBlockPos())

    fun NonNullContext.isIntersectingBlock(pos: BlockPos): Boolean =
        pos.blockState.block != Blocks.AIR && player.boundingBox.intersects(Box(pos))


}