package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object InventoryUtils {

    /**
     * Finds a block in the hotbar.
     * @param block The block to find.
     * @return The slot containing the block, or null if not found.
     */
    fun NonNullContext.findBlockInHotbar(block: Block): InventorySlot? {
        player.hotbarSlots.forEach { slot ->
            player.inventory.getItem(slot.hotbarSlot).let {
                it.item.let { item ->
                    if (item is BlockItem && item.block == block) return slot
                }
            }
        }
        return null
    }

    /**
     * Finds an item in the hotbar.
     * @param item The item to find.
     * @return The slot containing the item, or null if not found.
     */
    fun NonNullContext.findItemInHotbar(item: Item): InventorySlot? {
        player.hotbarSlots.forEach { slot ->
            player.inventory.getItem(slot.hotbarSlot).let {
                if (it.item == item) return slot
            }
        }
        return null
    }

}