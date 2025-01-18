package dev.exceptionteam.sakura.mixins.core.chat;

import dev.exceptionteam.sakura.asm.IGuiMessageLine;
import net.minecraft.client.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = { GuiMessage.class, GuiMessage.Line.class })
public class GuiMessageLineMixin implements IGuiMessageLine {
    @Unique
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
