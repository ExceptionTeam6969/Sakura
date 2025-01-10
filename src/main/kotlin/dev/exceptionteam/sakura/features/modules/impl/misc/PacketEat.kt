package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack

object PacketEat: Module(
    name = "PacketEat",
    category = Category.MISC,
) {
    init {
        nonNullListener<PacketEvents.Send> { e ->
            if (mc.player != null) {
                if (e.packet is ServerboundPlayerActionPacket && e.packet.action === ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM) {
                    val activeItem: ItemStack = player.useItem
                    val foodComponent: FoodProperties? = activeItem.item.components().get(DataComponents.FOOD)
                    if (foodComponent != null) {
                        e.cancel()
                    }
                }
            }
        }
    }
}