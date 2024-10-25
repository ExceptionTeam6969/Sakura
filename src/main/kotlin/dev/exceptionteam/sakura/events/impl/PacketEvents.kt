package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.CancellableEvent
import dev.exceptionteam.sakura.events.Event
import net.minecraft.network.packet.Packet

sealed class PacketEvents {

    class Receive(val packet: Packet<*>): CancellableEvent()

    class Send(val packet: Packet<*>): CancellableEvent()

    class PostSend(val packet: Packet<*>): Event()

    class PostReceive(val packet: Packet<*>): Event()

}