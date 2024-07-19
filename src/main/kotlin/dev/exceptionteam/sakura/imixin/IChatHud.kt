package dev.exceptionteam.sakura.imixin

import net.minecraft.text.Text

interface IChatHud {
    fun sakuraAddMessage(message: Text, id: Int)
}
