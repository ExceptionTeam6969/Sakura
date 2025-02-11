package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket

object PacketEat: Module(
    name = "packet-eat",
    category = Category.MISC,
) {
    init {
        nonNullListener<PacketEvents.Send> { e ->
            if (e.packet !is ServerboundPlayerActionPacket) return@nonNullListener

            if (e.packet.action != ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM) return@nonNullListener

            player.useItem.item.components().get(DataComponents.FOOD)?.let {
                e.cancel()
            }
        }
    }
}