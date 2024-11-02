package dev.exceptionteam.sakura.utils.world

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.BlockPos

object WorldUtils {

    val BlockPos.blockState get() = MinecraftClient.getInstance().world!!.getBlockState(this)

}