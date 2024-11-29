package dev.exceptionteam.sakura.asm

import net.minecraft.network.chat.Component

interface IChatHud {
    fun sakuraAddMessage(message: Component, id: Int)
}
