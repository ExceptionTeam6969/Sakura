package dev.exceptionteam.sakura.utils.player

data class InventorySlot(
    val slot: Int,
) {
    val hotbarSlot: Int get() = slot - 36
}