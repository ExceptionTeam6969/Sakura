package dev.exceptionteam.sakura.utils.world

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import kotlin.math.ceil
import kotlin.math.floor

object WorldUtils {

    val BlockPos.blockState: BlockState? get() =
        Minecraft.getInstance().level?.getBlockState(this)

    fun Level.checkBlockCollision(bb: AABB): Boolean {
        val j2 = floor(bb.minX).toInt()
        val k2 = ceil(bb.maxX).toInt()
        val l2 = floor(bb.minY).toInt()
        val i3 = ceil(bb.maxY).toInt()
        val j3 = floor(bb.minZ).toInt()
        val k3 = ceil(bb.maxZ).toInt()
        val mutable: BlockPos.MutableBlockPos = BlockPos.MutableBlockPos()
        for (l3 in j2..<k2) {
            for (i4 in l2..<i3) {
                for (j4 in j3..<k3) {
                    val state: BlockState = this.getBlockState(mutable.set(l3, i4, j4))
                    if (state.isAir) {
                        return true
                    }
                }
            }
        }
        return false
    }

}