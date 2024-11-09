package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.player.InventorySlot
import dev.exceptionteam.sakura.utils.interfaces.DirectTranslationEnum
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket

object HotbarManager {

    fun NonNullContext.silentSwitch(mode: SilentMode, slot: InventorySlot, func: () -> Unit) {
        when (mode) {
            SilentMode.PICK -> pickSilent(slot, func)
            SilentMode.SWAP -> swapSilent(slot, func)
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

    enum class SilentMode(override val key: CharSequence): DirectTranslationEnum {
        PICK("hotbar.silent-mode.pick"),
        SWAP("hotbar.silent-mode.swap"),
    }

}