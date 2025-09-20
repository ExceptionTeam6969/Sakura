package team.exception.sakura.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer

object Wrapper {

    val mc: Minecraft = Minecraft.getInstance()

    val player: LocalPlayer? get() = mc.player

    val level: ClientLevel? get() = mc.level

}