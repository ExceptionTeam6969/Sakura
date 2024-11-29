package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.math.toBlockPos
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB

object PlayerUtils {

    fun NonNullContext.isMoving() =
        player.xxa != 0f || player.yya != 0f || player.zza != 0f

    fun NonNullContext.isInBlock(): Boolean =
        isIntersectingBlock(player.blockPosition())
            || isIntersectingBlock(player.position().add(0.3, 0.0, 0.3).toBlockPos())
            || isIntersectingBlock(player.position().add(-0.3, 0.0, 0.3).toBlockPos())
            || isIntersectingBlock(player.position().add(-0.3, 0.0, -0.3).toBlockPos())
            || isIntersectingBlock(player.position().add(0.3, 0.0, -0.3).toBlockPos())

    fun NonNullContext.isIntersectingBlock(pos: BlockPos): Boolean =
        pos.blockState.block != Blocks.AIR && player.boundingBox.intersects(AABB(pos))


}