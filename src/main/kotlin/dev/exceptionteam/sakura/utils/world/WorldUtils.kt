package dev.exceptionteam.sakura.utils.world

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos

object WorldUtils {

    val BlockPos.blockState get() = Minecraft.getInstance().level!!.getBlockState(this)

}