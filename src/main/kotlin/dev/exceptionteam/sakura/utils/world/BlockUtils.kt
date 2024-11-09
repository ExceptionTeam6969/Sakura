package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

object BlockUtils {

    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction {
        return Direction.UP
    }

}