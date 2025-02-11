package dev.exceptionteam.sakura.utils.math.raytrace

import dev.exceptionteam.sakura.utils.math.raytrace.FastRayTraceAction.CALC
import dev.exceptionteam.sakura.utils.math.raytrace.FastRayTraceAction.SKIP
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

fun interface FastRayTraceFunction {
    operator fun Level.invoke(
        pos: BlockPos,
        state: BlockState,
        current: Vec3
    ): FastRayTraceAction

    companion object {
        @JvmField
        val DEFAULT = FastRayTraceFunction { pos, state, _ ->
            if (!state.getShape(this, pos).isEmpty) {
                CALC
            } else {
                SKIP
            }
        }
    }
}

fun interface RayTraceFunction {
    operator fun invoke(
        pos: Vec3
    ): FastRayTraceAction
}