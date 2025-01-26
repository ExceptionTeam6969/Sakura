package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.HotbarManager
import dev.exceptionteam.sakura.managers.impl.HotbarManager.switch
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findItemInHotbar
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import net.minecraft.network.protocol.game.ServerboundUseItemPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

object AutoBottle: Module(
    name = "auto-Bottle",
    category = Category.COMBAT
) {
    private val pitch by setting("pitch", 80f, 0f..90f)
    private val strict by setting("strict", true)
    private val delay by setting("delay", 100, 0..8000)
    private val swing by setting("swing", true)
    private val switchMode by setting("switch-mode", HotbarManager.SwitchMode.PICK)
    private val timer = TimerUtils()
    private var xp = false

    init {
        onEnable {
            timer.reset()
            xp = false
        }

        onDisable {
            xp = false
        }
        nonNullListener<TickEvent.Update> {
            if (switchMode == HotbarManager.SwitchMode.OFF && player.mainHandItem.item != Items.EXPERIENCE_BOTTLE ) {
                disable()
                return@nonNullListener
            }
            if (strict) player.deltaMovement = Vec3.ZERO        // Reset movement
            if (xp) {
                addRotation(player.yRot, pitch, 100) {
                    val slot = findItemInHotbar(Items.EXPERIENCE_BOTTLE) ?: run {
                        ChatUtils.sendWarnMessage("Can't find ender pearl in hot bar! disabling...")
                        return@addRotation
                    }

                    switch(switchMode, slot) {
                        connection.send(ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0, player.yRot, pitch))
                        if (swing) player.swing(InteractionHand.MAIN_HAND)
                    }
                }
                xp = false
            } else {
                addRotation(player.yRot, pitch, 100) {
                    val slot = findItemInHotbar(Items.EXPERIENCE_BOTTLE) ?: return@addRotation

                    if (switchMode == HotbarManager.SwitchMode.SWITCH) player.inventory.selected = slot
                    if (swing) player.swing(InteractionHand.MAIN_HAND)
                }
               xp = true
            }
            if (!timer.passedAndReset(delay)) return@nonNullListener
            disable()
        }
    }
}