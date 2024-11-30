package dev.exceptionteam.sakura.asm

import net.minecraft.network.chat.Component

interface IChatComponent {
    fun sakuraAddMessage(message: Component, id: Int)
}
