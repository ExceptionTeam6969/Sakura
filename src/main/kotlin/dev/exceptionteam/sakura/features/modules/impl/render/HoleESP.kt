package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.features.modules.impl.combat.HolePush
import dev.exceptionteam.sakura.features.modules.impl.combat.HolePush.setting
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import dev.exceptionteam.sakura.utils.world.aroundBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks

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

    init {
        nonNullListener<Render3DEvent> {
            player.blockPosition().aroundBlock(range, yRange).forEach {
                val pos: BlockPos = it
                if (!isHole(pos)) return@forEach
                renderer.add(pos, color)
                renderer.render(true, box, height, outline, lineWidth, height)
            }
        }
    }

    private fun isHole(pos: BlockPos): Boolean {
        val blockS = pos.blockState ?: return false
        if (blockS.block != Blocks.AIR) return false
        Direction.entries
            .filter { it != Direction.DOWN && it != Direction.UP }
            .forEach {
                val position = pos.relative(it)
                val blockState = position.blockState ?: return@forEach
                val block = blockState.block
                if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) return false
            }
        return true
    }

}