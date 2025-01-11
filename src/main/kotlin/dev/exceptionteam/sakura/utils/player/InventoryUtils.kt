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
    fun NonNullContext.findBlockInHotbar(block: Block): Int? {
        for (slot in 0..8) {
            player.inventory.getItem(slot).let {
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
    fun NonNullContext.findItemInHotbar(item: Item): Int? {
        for (slot in 0..8) {
            player.inventory.getItem(slot).let {
                if (it.item == item) return slot
            }
        }
        return null
    }

    fun NonNullContext.findItemInInventory(item: Item): Int? {
        for (i in 0 until 45) {
            if (player.inventory.getItem(i).item == item) {
                return if (i < 9) i + 36 else i
            }
        }
        return null
    }

    fun NonNullContext.findBlockInInventory(block: Block): Int? {
        for (i in 0 until 45) {
            if (player.inventory.getItem(i).item is BlockItem &&
                (player.inventory.getItem(i).item as BlockItem).block == block
            ) {
                return if (i < 9) i + 36 else i
            }
        }
        return null
    }

}