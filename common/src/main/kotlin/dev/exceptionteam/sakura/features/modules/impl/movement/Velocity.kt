package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.RotationManager.rotationPitch
import dev.exceptionteam.sakura.managers.impl.RotationManager.rotationYaw
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.player.PlayerUtils.isInBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.*
import net.minecraft.world.phys.Vec3
import java.util.*

object Velocity: Module(
    name = "velocity",
    category = Category.MOVEMENT
) {

    val noPush by setting("no-push", true)
    private val mode by setting("mode", Mode.NORMAL)
    private val fire by setting("anti-fire", true)
    private var flag = false
    private var cool  = 0
    var shouldCancelExplosion = false; private set

    init {

        nonNullListener<TickEvents.Post> {
            //anti fire lag
            if (player.isOnFire && fire && (player.hurtTime > 0)) {
                return@nonNullListener
            }
            //CPvp.cc bypass
            if (cool > 0) {
                cool--
            }
            //Grim站立全反 已失效 grim更新
            if (flag) {
                val sprinting = player.isSprinting
                if (cool <= 0) {
                    connection.send(
                        ServerboundMovePlayerPacket.PosRot(
                            player.x,
                            player.y,
                            player.z,
                            rotationYaw,
                            //Grim BaBzPacket QAQ + 90
                            rotationPitch + 90,
                            false,
                            true,
                        )
                    )
                    if(!sprinting) {
                        val p = BlockPos(
                            player.x.toInt(),
                            player.y.toInt() + 1,
                            player.z.toInt()
                        )
                        Objects.requireNonNull(connection).send(
                            ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                p,
                                Direction.DOWN
                            )
                        )
                    }
                }

                flag = false
            }
            shouldCancelExplosion = if (mode == Mode.WALL) isInBlock() else true
        }

        nonNullListener<PacketEvents.Receive> { e ->
            if (e.packet is ServerboundContainerClickPacket) {
                e.cancel()
            }
            when (mode) {
                Mode.NORMAL -> {
                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velocity = e.packet
                            if (velocity.id == player.id) {
                                e.cancel()
                            }
                        }
                    }
                }
                Mode.WALL -> {
                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velocity = e.packet
                            if (velocity.id == player.id) {
                                if (isInBlock()) e.cancel()
                            }
                        }
                    }
                }
                Mode.OLD_GRIM -> {
                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velocity = e.packet
                            if (velocity.id == player.id) {
                                if (isInBlock()) e.cancel()
                            }
                        }

                        is ClientboundExplodePacket -> {
                            val velocity: Vec3 = player.deltaMovement
                            // 创建一个新的速度向量
                            val newVelocity = Vec3(velocity.x - 1.254821222E-7, velocity.y - 1.91211299121E-7, velocity.z - 1.254821222E-7)
                            // 设置新的速度向量
                            player.deltaMovement = newVelocity
                            e.cancel()
                            flag = true
                        }
                    }
                }
                Mode.GRIM -> {
                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velocity = e.packet
                            if (velocity.id == player.id) {
                                if (isInBlock()) e.cancel()
                            }
                        }
                    }
                    val velocity: Vec3 = player.deltaMovement
                    // 创建一个新的速度向量
                    val newVelocity = Vec3(velocity.x - 1.254821222E-7, velocity.y - 1.91211299121E-7, velocity.z - 1.254821222E-7)
                    // 设置新的速度向量
                    player.deltaMovement = newVelocity
                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velX: Double = (e.packet.xa / 8000.0 - player.deltaMovement.x) * 0
                            val velY: Double = (e.packet.ya / 8000.0 - player.deltaMovement.y) * 0
                            val velZ: Double = (e.packet.za / 8000.0 - player.deltaMovement.z) * 0
                            ((velX * 8000 + player.deltaMovement.x * 8000).toInt())
                            ((velY * 8000 + player.deltaMovement.y * 8000).toInt())
                            ((velZ * 8000 + player.deltaMovement.z * 8000).toInt())
                            player.deltaMovement = newVelocity
                        }
                    }
                    player.deltaMovement = newVelocity
                }
                Mode.HYPIXEL -> {
                    when (val packet = e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            if (packet.id == player.id) { // 确保这个速度包是针对玩家自己的
                                e.cancel()
                                val newVelocity = Vec3(0.0, packet.ya, 0.0)
                                player.deltaMovement = newVelocity
                            }
                        }
                    }
                }
            }
        }
    }

    private enum class Mode(override val key: CharSequence): TranslationEnum {
        NORMAL("normal"),
        WALL("wall"),
        OLD_GRIM("old-grim"),
        GRIM("grim"),
        HYPIXEL("hypixel")
    }
}
