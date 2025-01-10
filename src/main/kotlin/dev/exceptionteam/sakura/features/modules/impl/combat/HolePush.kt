package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTarget
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.math.RotationUtils.getRotationTo
import dev.exceptionteam.sakura.utils.math.toBlockPos
import dev.exceptionteam.sakura.utils.player.InteractionUtils.placeBlock
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks

object HolePush: Module(
    name = "HolePush",
    category = Category.COMBAT
) {
    private val onlyPlayers by setting("only-players", true)
    private val rotation by setting("rotation", true)
    private val targetRange by setting("targetRange", 3.0f, 2.5f..6.0f)
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val swing by setting("swing", true)
    init {
        nonNullListener<TickEvent.Update> {
            if (onlyPlayers) getTargetPlayer(targetRange)?.let {
                pl(it)
            } else getTarget(targetRange)?.let {
                pl(it)
            }
        }
    }
    private fun NonNullContext.pl(target: Entity) {
        val rotAngle = getRotationTo(target.position())
        addRotation(rotAngle, 0, rotation) {
            //加点判断 比如target.position().add(0.0,0.0,1.0).toBlockPos()这个位置是air在run这个target.position().add(0.0,0.0,1.0).toBlockPos()
            placeBlock(target.position().add(0.0,0.0,1.0).toBlockPos(), Blocks.REDSTONE_BLOCK, switchMode, swing, rotation, 0)
            //活塞前加一个0度yaw转头
            placeBlock(target.position().add(0.0,1.0,1.0).toBlockPos(), Blocks.PISTON, switchMode, swing, rotation, 0)
            //这个也是 如果第一个跑了这个就别跑了 如果这个跑了在跑第一个
            placeBlock(target.position().add(0.0,2.0,1.0).toBlockPos(), Blocks.REDSTONE_BLOCK, switchMode, swing, rotation, 0)
            //然后写4个方向 按这个逻辑加点判断杂七杂八的就行了 比如地面放置啊 不能跑放啊 然后就完事了gg

        }
    }
}
