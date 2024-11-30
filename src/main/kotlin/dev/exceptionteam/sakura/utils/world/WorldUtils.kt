package dev.exceptionteam.sakura.utils.world

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

object WorldUtils {

    val BlockPos.blockState: BlockState get() =
        Minecraft.getInstance().level!!.getBlockState(this)

}