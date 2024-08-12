package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import net.minecraft.client.gui.DrawContext

class Render2DEvent(
    val context: DrawContext
): Event()