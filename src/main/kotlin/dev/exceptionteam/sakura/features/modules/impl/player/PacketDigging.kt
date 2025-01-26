package dev.exceptionteam.sakura.features.modules.impl.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PlayerDamageBlockEvent
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.managers.impl.HotbarManager.switch
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findItemInHotbar
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.canBreak
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks


object PacketDigging: Module(
    name = "packet-digging",
    category = Category.PLAYER
) {
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val color by setting("color", ColorRGB(255, 50, 50))
    private var breakPos: BlockPos? = null
    private val godBlocks: List<Block> = listOf(
        Blocks.AIR, Blocks.LAVA, Blocks.WATER, Blocks.BEDROCK
    )
    private val breakSuccess = TimerUtils()
    private var facing: Direction? = null
    private val renderer = ESPRenderer().apply { aFilled = 60 }

    init {
        nonNullListener<TickEvents.Update> {
            if (!godBlocks.contains(breakPos?.let { it1 -> world.getBlockState(it1).block })) {
                //鬼手失效
                val blockSlot = findItemInHotbar(Items.NETHERITE_PICKAXE) ?: return@nonNullListener
                switch(switchMode, blockSlot) {
                    mine()
                }
               // packerMine()
            }
        }

        nonNullListener<Render3DEvent> {
            breakPos?.let {
                renderer.add(breakPos!!, color)
                renderer.render(true)
            }
        }

        nonNullListener<PlayerDamageBlockEvent> { event ->
            if (event.pos?.let { canBreak(it) } == true) {
                breakPos = event.pos
                breakSuccess.reset()
                facing = event.facing
                if (breakPos != null) {
                    packetCat()
                }
            }
            event.cancel()
        }
    }
    private fun NonNullContext.packetCat(){
        player.swing(InteractionHand.MAIN_HAND)
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 ->
            connection.send(
                it2
            )
        }
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 ->
            connection.send(
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
            connection.send(
                it2
            )
        }
    }


    private fun NonNullContext.mine (){
        val blockSlot = findItemInHotbar(Items.NETHERITE_PICKAXE) ?: return
        if (breakPos?.let { it1 -> world.getBlockState(it1).block } == Blocks.OBSIDIAN) {
            if (breakSuccess.passedAndReset(1221)) {
                //ABORT Grim WoBreak
                switch(switchMode, blockSlot) {
                    player.swing(InteractionHand.MAIN_HAND)
                    breakPos?.let { it1 ->
                        facing?.let { it2 ->
                            ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                it1,
                                it2
                            )
                        }
                    }?.let { it2 ->
                        connection.send(
                            it2
                        )
                    }
                }
            } else {
                //秒挖stop包
                switch(switchMode, blockSlot) {
                    breakPos?.let { it1 ->
                        facing?.let { it2 ->
                            ServerboundPlayerActionPacket(
                                ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                                it1,
                                it2
                            )
                        }
                    }?.let { it2 ->
                        connection.send(
                            it2
                        )
                    }
                }
            }
        }
    }

    private fun NonNullContext.packerMine() {
        //双挖
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 ->
            connection.send(it2)
        }
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 ->
            connection.send(it2)
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
            connection.send(it2)
        }
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 -> connection.send(it2) }
        breakPos?.let { it1 ->
            facing?.let { it2 ->
                ServerboundPlayerActionPacket(
                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                    it1,
                    it2
                )
            }
        }?.let { it2 -> connection.send(it2) }

    }
}