package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
import dev.exceptionteam.sakura.utils.player.InteractionUtils.place
import dev.exceptionteam.sakura.utils.world.aroundBlock
import dev.exceptionteam.sakura.utils.world.hole.HoleUtils
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks

object HoleFiller: Module(
    name = "hole-filler",
    category = Category.COMBAT
) {

    private val BPT by setting("blocks-per-tick", 4, 1..8)
    private val placeRange by setting("place-range", 4.5, 1.0..6.0)
    private val rotate by setting("rotate", false)
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val swing by setting("swing", true)
    private val web by setting("web-fill", false)

    private var blocksPerTick = 0

    init {
        nonNullListener<TickEvents.Update> {
            val placeList = HashSet<BlockPos>()
            blocksPerTick = 0;
            player.blockPosition().aroundBlock(6, 6)
                .filter { it.center.distanceSqTo(player) <= placeRange.sq }
                .forEach { pos ->
                    if (HoleUtils.isSingleHole(pos)) {
                        placeList.add(pos)
                    }
                    HoleUtils.getDoubleHole(pos)?.let {
                        placeList.add(pos)
                        placeList.add(it)
                    }
                }
            placeList.forEach { placeBlock(it) }
        }
    }

    private fun NonNullContext.placeBlock(pos: BlockPos) {

        if (blocksPerTick < BPT) {
            val item = if (web) Blocks.COBWEB else Blocks.OBSIDIAN
            place(pos, item, switchMode, swing, rotate, 0)
            blocksPerTick++
        }


    }

}