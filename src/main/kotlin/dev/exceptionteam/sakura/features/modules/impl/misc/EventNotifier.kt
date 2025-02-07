package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.features.modules.impl.client.Language
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
            when (Language.language) {
                Language.Languages.EN_US -> {
                    ChatUtils.sendMessage("Player ${player.name} joined!")
                }
                Language.Languages.ZH_CN -> {
                    ChatUtils.sendMessage("玩家 ${player.name} 加入了伺服器!")
                }
            }
        }
    }

    private fun NonNullContext.sendLeaveMessage(packet: ClientboundPlayerInfoRemovePacket) {
        for (uuid in packet.profileIds) {
            val leave: PlayerInfo = connection.getPlayerInfo(uuid) ?: continue
            when (Language.language) {
                Language.Languages.EN_US -> {
                    ChatUtils.sendMessage("Player ${leave.profile.name} leaved!")
                }
                Language.Languages.ZH_CN -> {
                    ChatUtils.sendMessage("Player ${leave.profile.name} 离开了伺服器!")
                }
            }
        }
    }
}