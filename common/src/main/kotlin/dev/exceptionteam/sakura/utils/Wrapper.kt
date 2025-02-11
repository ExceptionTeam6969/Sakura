package dev.exceptionteam.sakura.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer

object Wrapper {

    @JvmStatic val mc: Minecraft get() = Minecraft.getInstance()

    @JvmStatic val player: LocalPlayer? get() = mc.player

    @JvmStatic val world: ClientLevel? get() = mc.level

}