package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.TranslationManager
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket

object EventNotifier: Module(
    name = "event-notifier",
    category = Category.MISC
) {
    private val playerJoin by setting("player-join", true)
    private val playerLeave by setting("player-leave", true)

    private const val NAME = "\$NAME"

    init {
        nonNullListener<PacketEvents.Receive> { it ->
            if (it.packet is ClientboundPlayerInfoUpdatePacket && playerJoin) {
                val packet: ClientboundPlayerInfoUpdatePacket = it.packet
                if (!packet.actions().contains(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER)) return@nonNullListener
                sendJoinMessage(packet)
            }
            if (it.packet is ClientboundPlayerInfoRemovePacket && playerLeave) {
                val packet: ClientboundPlayerInfoRemovePacket = it.packet
                sendLeaveMessage(packet)
            }
        }
    }

    private fun sendJoinMessage(packet: ClientboundPlayerInfoUpdatePacket) {
        for (entry in packet.entries()) {
            val player = entry.profile ?: continue

            val joinMessage =
                TranslationManager.getTranslation("modules.event-notifier.join-message")
                    .replace(NAME, player.name)

            ChatUtils.sendMessage(joinMessage)
        }
    }

    private fun NonNullContext.sendLeaveMessage(packet: ClientboundPlayerInfoRemovePacket) {
        for (uuid in packet.profileIds) {
            val leave: PlayerInfo = connection.getPlayerInfo(uuid) ?: continue

            val leaveMessage =
                TranslationManager.getTranslation("modules.event-notifier.leave-message")
                    .replace(NAME, leave.profile.name)

            ChatUtils.sendMessage(leaveMessage)
        }
    }
}