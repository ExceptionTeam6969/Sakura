package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import dev.exceptionteam.sakura.utils.world.aroundBlock
import dev.exceptionteam.sakura.utils.world.hole.HoleType
import dev.exceptionteam.sakura.utils.world.hole.HoleUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB

object HoleESP: Module(
    name = "hole-esp",
    category = Category.RENDER
) {

    private val range by setting("range", 8, 0..15)
    private val yRange by setting("y-range", 3, 0..6)
    private val box by setting("box", true)
    private val outline by setting("outline", true)
    private val height by setting("height", 0.1, 0.1..1.0)
    private val lineWidth by setting("line-width", 1.0f, 0.1f..5.0f) { outline }
    private val color by setting("color", ColorRGB(255, 153, 51))

    private val renderer = ESPRenderer().apply { aFilled = 60 }
    private var renderedBox = mutableListOf<AABB>()

    init {
        nonNullListener<Render3DEvent> {
            var colorRGB = color
            player.blockPosition().aroundBlock(range, yRange).forEach { pos ->
                val info: HoleUtils.HoleInfo = HoleUtils.getHoleType(pos) ?: return@forEach
                val aabb = info.box
                val type = info.holeType
                if (type == HoleType.DOUBLE) {
                    colorRGB = ColorRGB(color.r, color.g, color.b, color.a / 2)
                }
                renderer.add(aabb, colorRGB)
                renderer.render(true, box, height, outline, lineWidth, height)
            }
        }
    }
}