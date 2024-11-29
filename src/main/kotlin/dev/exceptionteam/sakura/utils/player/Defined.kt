package dev.exceptionteam.sakura.utils.player

import net.minecraft.world.entity.player.Player

inline val Player.hotbarSlots: List<InventorySlot>
    get() = ArrayList<InventorySlot>().apply {
        for (slot in 36..44) {
            add(InventorySlot(slot))
        }
    }