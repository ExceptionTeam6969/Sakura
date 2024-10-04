package dev.exceptionteam.sakura.utils.memory

import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import java.nio.ByteOrder

val memStack get() = MemoryStack.stackPush()

fun createDirectByteBuffer(capacity: Int): ByteBuffer {
    return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder())
}