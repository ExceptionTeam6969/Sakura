package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.player.InventorySlot
import dev.exceptionteam.sakura.utils.interfaces.DirectTranslationEnum
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket

object HotbarManager {

    fun NonNullContext.switch(mode: SwitchMode, slot: Int, func: () -> Unit) {
        when (mode) {
            SwitchMode.PICK -> pickSilent(slot, func)
            SwitchMode.SWAP -> swapSilent(slot, func)
            SwitchMode.SWITCH -> switchTo(slot, func)
            SwitchMode.OFF -> if (slot == player.inventory.selected) func()
        }
    }

    fun NonNullContext.pickSilent(slot: Int, func: () -> Unit) {
        val lastSlot = player.inventory.selected
        if (slot != lastSlot) connection.send(ServerboundSetCarriedItemPacket(slot))
        func()
        if (slot != lastSlot) connection.send(ServerboundSetCarriedItemPacket(lastSlot))
    }

    fun NonNullContext.swapSilent(slot: Int, func: () -> Unit) {
        // TODO: swap silent
        func()
        // TODO: restore slot
    }

    fun NonNullContext.switchTo(slot: Int, func: () -> Unit) {
        if (player.inventory.selected != slot) {
            player.inventory.selected = slot
        }
        func()
    }

    enum class SwitchMode(override val key: CharSequence): DirectTranslationEnum {
        OFF("hotbar.switch-mode.off"),
        SWITCH("hotbar.switch-mode.switch"),
        PICK("hotbar.switch-mode.pick"),
        SWAP("hotbar.switch-mode.swap"),
    }

}