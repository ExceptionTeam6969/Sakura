package dev.exceptionteam.sakura.mixins.core.network;

import dev.exceptionteam.sakura.events.impl.PacketEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;Z)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacketPre(Packet<?> packet, PacketSendListener packetSendListener, boolean bl, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.Send event = new PacketEvents.Send(packet);
            event.post();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;Z)V", at = @At("RETURN"))
    public void onSendPacketPost(Packet<?> packet, PacketSendListener packetSendListener, boolean bl, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.PostSend event = new PacketEvents.PostSend(packet);
            event.post();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onChannelReadHead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.Receive event = new PacketEvents.Receive(packet);
            event.post();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("RETURN"))
    public void onChannelReadReturn(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.PostReceive event = new PacketEvents.PostReceive(packet);
            event.post();
        }
    }

}
