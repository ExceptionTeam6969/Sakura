package dev.exceptionteam.sakura.mixins.chat;

import dev.exceptionteam.sakura.asm.IChatHudLine;
import net.minecraft.client.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = {GuiMessage.class, GuiMessage.Line.class})
public class GuiMessageLineMixin implements IChatHudLine {
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
