package dev.exceptionteam.sakura.utils.memory

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun createDirectByteBuffer(capacity: Int): ByteBuffer {
    return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder())
}