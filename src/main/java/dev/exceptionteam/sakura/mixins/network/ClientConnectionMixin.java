package dev.exceptionteam.sakura.mixins.network;

import dev.exceptionteam.sakura.events.impl.PacketEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = {"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"}, at = @At("HEAD"), cancellable = true)
    public void onSendPacketPre(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.Send event = new PacketEvents.Send(packet);
            event.post();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }


    @Inject(method = {"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"}, at = @At("RETURN"), cancellable = true)
    public void onSendPacketPost(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.PostSend event = new PacketEvents.PostSend(packet);
            event.post();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onChannelReadHead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.Receive event = new PacketEvents.Receive(packet);
            event.post();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("RETURN"))
    public void onChannelReadReturn(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet != null) {
            PacketEvents.PostReceive event = new PacketEvents.PostReceive(packet);
            event.post();
        }
    }

}
