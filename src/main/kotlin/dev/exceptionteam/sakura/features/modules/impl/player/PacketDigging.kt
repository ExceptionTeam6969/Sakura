package dev.exceptionteam.sakura.features.modules.impl.player

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.PlayerDamageBlockEvent
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.canBreak
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.*


object PacketDigging: Module(
    name = "PacketDigging",
    category = Category.PLAYER
) {
    private var breakPos: BlockPos? = null
     private val godBlocks: List<Block> = listOf(
        Blocks.AIR,  Blocks.LAVA,  Blocks.WATER, Blocks.BEDROCK
    )
    private val breakSuccess = TimerUtils()
    private var facing: Direction? = null
    init {
        nonNullListener<TickEvent.Update> {
            if (!godBlocks.contains(breakPos?.let { it1 -> world.getBlockState(it1).block })) {
                //准备挖掘
                //OBSIDIAN
                if (breakPos?.let { it1 -> world.getBlockState(it1).block } == Blocks.OBSIDIAN) {
                    if (breakSuccess.passedAndReset(1234)) {
                        breakPos?.let { it1 ->
                            facing?.let { it2 ->
                                ServerboundPlayerActionPacket(
                                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                    it1,
                                    it2
                                )
                            }
                        }?.let { it2 ->
                            Objects.requireNonNull(connection).send(
                                it2
                            )
                        }
                    }else{
                        breakPos?.let { it1 ->
                            facing?.let { it2 ->
                                ServerboundPlayerActionPacket(
                                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                    it1,
                                    it2
                                )
                            }
                        }?.let { it2 ->
                            Objects.requireNonNull(connection).send(
                                it2
                            )
                        }
                    }
                }
            }
        }
        nonNullListener<PacketEvents.Send> { event ->
            if (event.packet is ServerboundPlayerActionPacket) {
                val packet: ServerboundPlayerActionPacket = event.packet
                if (packet.action === ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                    event.cancel()
                }
            }
        }

        nonNullListener<PlayerDamageBlockEvent> { event ->
            if (event.pos?.let { canBreak(it) } == true) {
                breakPos = event.pos
                breakSuccess.reset()
                facing = event.facing
                if (breakPos != null) {

                    breakPos?.let { it1 ->
                        facing?.let { it2 ->
                            ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                                it1,
                                it2
                            )
                        }
                    }?.let { it2 ->
                        Objects.requireNonNull(connection).send(
                            it2
                        )
                    }
                    breakPos?.let { it1 ->
                        facing?.let { it2 ->
                            ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                it1,
                                it2
                            )
                        }
                    }?.let { it2 ->
                        Objects.requireNonNull(connection).send(
                            it2
                        )
                    }
                }
            }
        }



    }
}