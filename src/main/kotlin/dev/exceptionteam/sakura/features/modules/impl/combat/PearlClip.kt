package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.HotbarManager.SwitchMode
import dev.exceptionteam.sakura.managers.impl.HotbarManager.switch
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findItemInHotbar
import net.minecraft.network.protocol.game.ServerboundUseItemPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Items

object PearlClip: Module(
    name = "pearl-clip",
    category = Category.COMBAT
) {

    private val pitch by setting("pitch", 80f, 0f..90f)
    private val switchMode by setting("switch-mode", SwitchMode.PICK)

    private var secondTickFlag = false

    init {

        onEnable {
            secondTickFlag = false
        }

        onDisable {
            secondTickFlag = false
        }

        nonNullListener<TickEvent.Update> {
            if (switchMode == SwitchMode.OFF && player.mainHandItem.item != Items.ENDER_PEARL) {
                disable()
                return@nonNullListener
            }

            if (secondTickFlag) {
                addRotation(player.yRot, pitch, 100) {
                    val slot = findItemInHotbar(Items.ENDER_PEARL) ?: run {
                        ChatUtils.sendWarnMessage("Can't find ender pearl in hotbar! disabling...")
                        return@addRotation
                    }

                    switch(switchMode, slot) {
                        connection.send(ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0, player.yRot, pitch))
                    }
                }
                secondTickFlag = false
                disable()
            } else {
                addRotation(player.yRot, pitch, 100)
                secondTickFlag = true
            }

        }

    }

}