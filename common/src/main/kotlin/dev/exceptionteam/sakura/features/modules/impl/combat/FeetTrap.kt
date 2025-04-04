package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.utils.player.InteractionUtils.place
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks

object FeetTrap: Module(
    name = "feet-trap",
    category = Category.COMBAT
) {

    private val rotation by setting("rotation", true)
    private val multiPlace by setting("multi-place", 2, 1..4) { !rotation}
    private val delay by setting("delay", 100, 0..1000)
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val swing by setting("swing", true)

    private val timer = TimerUtils()

    init {

        onEnable {
            timer.reset()
        }

        nonNullListener<TickEvents.Update> {
            if (!timer.passedAndReset(delay)) return@nonNullListener

            var multiCount = 0

            Direction.entries
                .filter { it != Direction.DOWN && it != Direction.UP }
                .forEach { dir ->
                    val pos = player.blockPosition().relative(dir)   // Position to place block
                    val blockState = pos.blockState ?: return@forEach
                    val block = blockState.block

                    if (block != Blocks.AIR) return@forEach  // Skip if block is not air

                    if (rotation && multiCount > 0) return@nonNullListener
                    if (multiCount > multiPlace) return@nonNullListener

                    place(pos, Blocks.OBSIDIAN, switchMode, swing, rotation, 0)

                    multiCount++
                }
        }

    }

}