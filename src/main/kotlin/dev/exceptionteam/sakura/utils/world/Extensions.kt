package dev.exceptionteam.sakura.utils.world

import net.minecraft.core.BlockPos

fun BlockPos.aroundBlock(range: Int): List<BlockPos> {
    val result = mutableListOf<BlockPos>()

    for (x in -range..range) {
        for (y in -range..range) {
            for (z in -range..range) {
                result.add(this.offset(x, y, z))
            }
        }
    }

    return result
}
