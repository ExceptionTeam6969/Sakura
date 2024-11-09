package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import net.minecraft.client.MinecraftClient

class Render2DEvent(
    val tickDelta: Float = MinecraftClient.getInstance().renderTickCounter.getTickDelta(true)
): Event()