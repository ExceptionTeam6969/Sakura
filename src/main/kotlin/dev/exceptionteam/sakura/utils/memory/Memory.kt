package dev.exceptionteam.sakura.utils.memory

import org.lwjgl.system.MemoryStack

val memStack get() = MemoryStack.stackPush()