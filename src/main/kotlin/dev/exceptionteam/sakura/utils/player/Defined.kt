package dev.exceptionteam.sakura.utils.player

import net.minecraft.entity.player.PlayerEntity

inline val PlayerEntity.hotbarSlots: List<InventorySlot>
    get() = ArrayList<InventorySlot>().apply {
        for (slot in 36..44) {
            add(InventorySlot(slot))
        }
    }