package dev.exceptionteam.sakura.mixins.chat;

import dev.exceptionteam.sakura.asm.IChatHud;
import dev.exceptionteam.sakura.asm.IChatHudLine;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin implements IChatHud {

    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;

    @Shadow
    @Final
    private List<GuiMessage> allMessages;

    @Unique
    private int nextId;

    @Shadow
    public abstract void addMessage(Component message);

    @Override
    public void sakuraAddMessage(Component message, int id) {
        nextId = id;
        addMessage(message);
        nextId = 0;
    }

    @Inject(method = "addMessageToQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(GuiMessage message, CallbackInfo ci) {
        ((IChatHudLine) (Object) trimmedMessages.getFirst()).setId(nextId);
    }

    @Inject(method = "addMessageToDisplayQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(GuiMessage message, CallbackInfo ci) {
        ((IChatHudLine) (Object) allMessages.getFirst()).setId(nextId);
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V")
    private void onAddMessage(Component chatComponent, MessageSignature headerSignature, GuiMessageTag tag, CallbackInfo ci) {

        try {
            trimmedMessages.removeIf(msg -> ((IChatHudLine) (Object) msg).getId() == nextId && nextId != 0);

            for (int i = allMessages.size() - 1; i > -1; i--) {
                if (((IChatHudLine) (Object) allMessages.get(i)).getId() == nextId && nextId != 0) {
                    allMessages.remove(i);
                }
            }
        } catch (Exception ignored) {}

    }

}