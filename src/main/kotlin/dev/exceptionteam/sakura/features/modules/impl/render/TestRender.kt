package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.RenderUtils3D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import net.minecraft.util.math.Box

object TestRender: Module(
    name = "test-render",
    category = Category.RENDER
) {

    init {

        nonNullListener<Render3DEvent> {

            RenderUtils3D.drawFilledBox(Box(player.blockPos.up(3)), ColorRGB.WHITE.alpha(0.6f))

        }

    }

}