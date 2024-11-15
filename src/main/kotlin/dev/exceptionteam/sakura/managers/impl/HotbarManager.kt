package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.player.InventorySlot
import dev.exceptionteam.sakura.utils.interfaces.DirectTranslationEnum
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket

object HotbarManager {

    fun NonNullContext.switch(mode: SwitchMode, slot: InventorySlot, func: () -> Unit) {
        when (mode) {
            SwitchMode.PICK -> pickSilent(slot, func)
            SwitchMode.SWAP -> swapSilent(slot, func)
            SwitchMode.SWITCH -> switchTo(slot, func)
            SwitchMode.OFF -> if (slot.hotbarSlot == player.inventory.selectedSlot) func()
        }
    }

    fun NonNullContext.pickSilent(slot: InventorySlot, func: () -> Unit) {
        val lastSlot = player.inventory.selectedSlot
        connection.sendPacket(UpdateSelectedSlotC2SPacket(slot.hotbarSlot))
        func()
        connection.sendPacket(UpdateSelectedSlotC2SPacket(lastSlot))
    }

    fun NonNullContext.swapSilent(slot: InventorySlot, func: () -> Unit) {
        // TODO: swap silent
        func()
        // TODO: restore slot
    }

    fun NonNullContext.switchTo(slot: InventorySlot, func: () -> Unit) {
        player.inventory.selectedSlot = slot.hotbarSlot
        func()
    }

    enum class SwitchMode(override val key: CharSequence): DirectTranslationEnum {
        OFF("hotbar.switch-mode.off"),
        SWITCH("hotbar.switch-mode.switch"),
        PICK("hotbar.switch-mode.pick"),
        SWAP("hotbar.switch-mode.swap"),
    }

}