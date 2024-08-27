package dev.exceptionteam.sakura.asm

import net.minecraft.text.Text

interface IChatHud {
    fun sakuraAddMessage(message: Text, id: Int)
}
