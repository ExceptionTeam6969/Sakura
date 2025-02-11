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

    private val renderer = ESPRenderer().apply { aFilled = 60 }
    private var pistonInfo: PistonInfo? = null
    private var multiCount = 0
    private val timer = TimerUtils()
    private var stage = 0

    init {
        onEnable {
            multiCount = 0
            stage = 0
            timer.reset()
            pistonInfo = null
        }

        nonNullListener<Render3DEvent> {
            pistonInfo?.let {
                renderer.add(it.pistonPos, pistonColor)
                renderer.render(true)
            }
        }

        nonNullListener<TickEvents.Update> {
            if (movePause && isMoving()) return@nonNullListener
            if (onlyPlayers) getTargetPlayer(targetRange)?.let {
                push(it)
            } else getTarget(targetRange)?.let {
                push(it)
            }
        }

    }

    private fun NonNullContext.push(target: Entity) {
        pistonInfo = getPiston(target.blockPosition()) ?: return

        val pistonPos: BlockPos = pistonInfo?.pistonPos ?: return
        val pistonFacing: Direction = pistonInfo?.direction ?: return
        val redstonePos: BlockPos = pistonInfo?.redstonePos ?: return

        when (stage) {
            0 -> {
                // Piston's direction
                val angle = Direction.getYRot(pistonFacing.opposite)
                addRotation(angle, 0.0f, 0)
                nextStage()
            }
            1 -> {
                // Place piston
                place(pistonPos, Blocks.PISTON, switchMode, swing, rotation, 0)

                nextStage()
            }
            2 -> {
                // Place red stone
                if (!timer.passedAndReset(delay)) return

                place(redstonePos, Blocks.REDSTONE_BLOCK, switchMode, swing, rotation, 0)

                disable()
                return
            }
        }
    }

    private fun nextStage() {
        multiCount++
        stage++
    }

    private fun NonNullContext.getPiston(playerPos: BlockPos): PistonInfo? {
        Direction.entries
            .filter { it != Direction.DOWN && it != Direction.UP }
            .forEach { direction ->
                val pos = playerPos.above().relative(direction)
                val blockState = pos.blockState ?: return@forEach
                val block = blockState.block
                if (block != Blocks.AIR && block != Blocks.PISTON) return@forEach //not air = 放你妈
                if (getNeighbourSide(pos) == null) return@forEach
                if (block == Blocks.PISTON && isActivated(blockState)) return@forEach
                val redstone = getRedStone(pos, direction.opposite, playerPos.above()) ?: return@forEach
                return PistonInfo(pos, direction.opposite, redstone) //返回的是活塞要看着的方向
            }
        return null
    }

     private fun isActivated(state: BlockState):Boolean {
         return state.block == Blocks.PISTON && state.getValue(PistonBaseBlock.EXTENDED)
     }

    //TODO:其实我在考虑这个pos变数要不要搞成list 这样能一次把不想红石放的位置黑名单
    private fun getRedStone(pistonPos: BlockPos, pistonFacing: Direction, blacklist: BlockPos): BlockPos? {
        Direction.entries
            .filter { it != pistonFacing }
            .forEach { direction ->
                val pos = pistonPos.relative(direction)
                val blockState = pos.blockState ?: return@forEach
                val block = blockState.block
                if (block != Blocks.AIR) return@forEach
                if (pos == blacklist) return@forEach
                return pos
            }
        return null
    }

    data class PistonInfo(
        val pistonPos: BlockPos,
        val direction: Direction,
        val redstonePos: BlockPos,
    )
}
