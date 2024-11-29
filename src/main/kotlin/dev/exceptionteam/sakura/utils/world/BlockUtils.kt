package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

object BlockUtils {

    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction {
        return Direction.UP
    }

}