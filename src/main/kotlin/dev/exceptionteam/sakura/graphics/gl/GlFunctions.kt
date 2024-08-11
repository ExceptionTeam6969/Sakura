package dev.exceptionteam.sakura.graphics.gl

import dev.luna5ama.kmogus.Arr
import dev.luna5ama.kmogus.Ptr
import dev.luna5ama.kmogus.nullByteBuffer
import org.lwjgl.opengl.GL45
import org.lwjgl.opengl.GL45C
import java.nio.ByteBuffer

fun glCreateBuffers(): Int {
    return GL45.glCreateBuffers()
}

fun glNamedBufferStorage(buffer: Int, dataSize: Long, pointer: Ptr, flags: Int) {
    GL45C.nglNamedBufferStorage(buffer, dataSize, pointer.address, flags)
}

fun glNamedBufferStorage(buffer: Int, dataSize: Long, data: Long, flags: Int) {
    GL45C.nglNamedBufferStorage(buffer, dataSize, data, flags)
}

fun glMapNamedBufferRange(buffer: Int, offset: Long, length: Long, access: Int): Arr {
    val byteBuffer = GL45.glMapNamedBufferRange(
        buffer, offset, length, access
    ) as ByteBuffer

    return Arr.wrap(byteBuffer)
}

/**
 * 用0填充buffer
 */
fun glClearNamedBufferData(buffer: Int, internalFormat: Int, format: Int, type: Int) {
    GL45.glClearNamedBufferData(buffer, internalFormat, format, type, nullByteBuffer())
}

fun glCreateVertexArrays(): Int {
    return GL45.glCreateVertexArrays()
}

fun glBindVertexArray(vao: Int) {
    GL45.glBindVertexArray(vao)
}

fun glDrawArrays(mode: Int, first: Int, count: Int) {
    GL45.glDrawArrays(mode, first, count)
}