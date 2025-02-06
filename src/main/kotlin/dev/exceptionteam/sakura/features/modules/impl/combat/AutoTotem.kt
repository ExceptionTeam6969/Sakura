package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findItemInInventory
import dev.exceptionteam.sakura.utils.player.InventoryUtils.getItemCountInInventory
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

object AutoTotem: Module(
    name = "auto-totem",
    category = Category.COMBAT
) {

    private val strict by setting("strict", true)
    private val health by setting("health", 12.0f, 0.0f..36.0f)

    private var totemCount = 0

    override fun hudInfo(): String? = "$totemCount"

    init {

        nonNullListener<TickEvents.Update> {
            if (player.health <= health && player.offhandItem.item != Items.TOTEM_OF_UNDYING) {
                val slot = findItemInInventory(Items.TOTEM_OF_UNDYING) ?: return@nonNullListener
                if (strict) player.deltaMovement = Vec3.ZERO        // Reset movement

                val offhandEmptyPreSwitch = player.offhandItem.item == Items.AIR

                clickSlot(slot)
                clickSlot(45)
                if (offhandEmptyPreSwitch) clickSlot(slot)
            }

            totemCount = getItemCountInInventory(Items.TOTEM_OF_UNDYING)
        }

    }

    private fun NonNullContext.clickSlot(slot: Int) =
        playerController.handleInventoryMouseClick(player.containerMenu.containerId, slot, 0, ClickType.PICKUP, player)

}