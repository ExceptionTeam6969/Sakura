package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft

class Render2DEvent(
    val tickDelta: Float = Minecraft.getInstance().timer.gameTimeDeltaTicks
): Event()