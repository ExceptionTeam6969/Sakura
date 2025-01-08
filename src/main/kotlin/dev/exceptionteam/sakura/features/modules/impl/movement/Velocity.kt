package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.RotationManager
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
    private val fire by setting("antiFire", true)
    private var flag = false
    private var Cool  = 0
    var shouldCancelExplosion = false; private set

    init {

        nonNullListener<TickEvent.Post> {
            //anti fire lag
            if (player.isOnFire && fire && (player.hurtTime > 0)) {
                return@nonNullListener
            }
            //CPvp.cc bypass
            if (Cool > 0) {
                Cool--
            }
            //Grim站立全反 已失效 grim更新
            if (flag) {
                val sprinting = player.isSprinting
                //player.lastSprinting
                if (Cool <= 0) {
                    connection.send(
                        ServerboundMovePlayerPacket.PosRot(
                            player.x,
                            player.y,
                            player.z,
                            RotationManager.rotationYaw,
                            //Grim BaBzPacket QAQ + 90
                            RotationManager.rotationPitch + 90,
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
                //player.setVelocity

                flag = false
            }
            shouldCancelExplosion = if (mode == Mode.WALL) isInBlock() else true


        }

        nonNullListener<PacketEvents.Receive> { e ->
            //if (e.packet is Client boundPlayerPositionPacket)
            //{
            //  Cool = 5
            //}
            //C0f
            if (e.packet is ServerboundContainerClickPacket)
            {
                e.cancel()
            }
            //S32
           // if (e.packet is S32PacketConfirmTransaction && player.hurtTime > 0)
           // {
           //     e.cancel()
           // }
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
                Mode.GRIM -> {

                    when (e.packet) {
                        is ClientboundSetEntityMotionPacket -> {
                            val velocity = e.packet
                            if (velocity.id == player.id) {
                                if (isInBlock()) e.cancel()
                            }
                        }
                    }

                    when (e.packet) {
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
            when (e.packet) {
                is ClientboundSetEntityMotionPacket -> {
                    val velocity = e.packet
                    if (velocity.id == player.id) {
                        e.cancel()
                        flag = true
                    }
                }
            }
        }
                Mode.SAKURA -> {
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
    }
}
}



    private enum class Mode(override val key: CharSequence): TranslationEnum {
        NORMAL("normal"),
        WALL("wall"),
        GRIM("grim"),
        SAKURA("sakura")
    }
}
