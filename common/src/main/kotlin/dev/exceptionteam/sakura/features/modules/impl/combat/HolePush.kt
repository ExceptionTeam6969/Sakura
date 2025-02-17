package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTarget
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import dev.exceptionteam.sakura.utils.player.InteractionUtils.place
import dev.exceptionteam.sakura.utils.player.PlayerUtils.isMoving
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.getNeighbourSide
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.piston.PistonBaseBlock
import net.minecraft.world.level.block.state.BlockState

/*
   !!! SHIT CODE WARNING !!!

   Author: @dragon-jpg
   This feature is still in beta. There will be more further update for it.
   If u discover a bug, please create an issue on GitHub, thanks.
*/
object HolePush: Module(
    name = "hole-push",
    category = Category.COMBAT
) {
    private val targetRange by setting("target-range", 3.0f, 2.5f..6.0f)
    private val onlyPlayers by setting("only-players", true)
    private val rotation by setting("rotation", true)
    private val delay by setting("delay", 100, 0..1000)
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val swing by setting("swing", true)
    private val movePause by setting("pause-on-moving", false)
    private val pistonColor by setting("piston-color", ColorRGB(255, 153, 51))
    private val redstoneColor by setting("redstone-color", ColorRGB(255, 25, 25))

    private val renderer = ESPRenderer().apply { aFilled = 60 }
    private var pistonPos: BlockPos? = null
    private var redstonePos: BlockPos? = null
    private var multiCount = 0
    private val timer = TimerUtils()
    private var stage = 0

    init {
        onEnable {
            multiCount = 0
            stage = 0
            timer.reset()
        }

        nonNullListener<Render3DEvent> {
            pistonPos?.let {
                renderer.add(it, pistonColor)
            }
            redstonePos?.let {
                renderer.add(it, redstoneColor)
            }
            renderer.render(true)
        }

        nonNullListener<TickEvents.Update> {
            if (movePause && isMoving()) return@nonNullListener
            if (!timer.passedAndReset(delay)) return@nonNullListener
            if (onlyPlayers) getTargetPlayer(targetRange)?.let {
                push(it)
            } else getTarget(targetRange)?.let {
                push(it)
            }
        }

    }

    private fun NonNullContext.push(target: Entity) {
        val facing = getPistonFacing(target) ?: return
        val piston = target.blockPosition().above().relative(facing)
        val redstone = getRedStone(piston, facing.opposite) ?: return
        pistonPos = piston
        redstonePos = redstone
        when (stage) {
            0 -> {
                val angle = Direction.getYRot(facing)
                addRotation(angle, 0.0f, 0)
                stage++
                return
            }
            1 -> {
                place(piston, Blocks.PISTON, switchMode, swing, rotation, 0)
                stage++
            }
            2 -> {
                place(redstone, Blocks.REDSTONE_BLOCK, switchMode, swing, rotation, 0)
                stage++
            }
            3 -> {
                toggle()
            }
        }
    }

    private fun NonNullContext.getPistonFacing(target: Entity): Direction? {
        Direction.entries.filter { it != Direction.UP && it != Direction.DOWN }
            .forEach { dir ->
                val pos = target.blockPosition().above().relative(dir)
                val state = pos.blockState ?: return@forEach
                val block = state.block
                if (block != Blocks.AIR && block != Blocks.PISTON) return@forEach
                if (getNeighbourSide(pos) == null) return@forEach
                if (getRedStone(pos, dir.opposite) == null) return@forEach
                return dir
            }
        return null
    }

    private fun NonNullContext.getRedStone(pistonPos: BlockPos, facing: Direction): BlockPos? {
        Direction.entries.filter { it != facing }
            .forEach { dir ->
                val pos = pistonPos.relative(dir)
                if (getNeighbourSide(pos) == null) return@forEach
                val state = pos.blockState ?: return@forEach
                val block = state.block
                if (block != Blocks.AIR) return@forEach
                return pos
            }
        return null
    }

}
