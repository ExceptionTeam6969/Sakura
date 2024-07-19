package dev.exceptionteam.sakura.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

object Wrapper {

    @JvmStatic val mc: MinecraftClient get() = MinecraftClient.getInstance()

    @JvmStatic val player: ClientPlayerEntity? get() = mc.player

    @JvmStatic val world: ClientWorld? get() = mc.world

}