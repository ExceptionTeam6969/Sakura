package dev.exceptionteam.sakura.graphics.gl

import dev.luna5ama.kmogus.Arr
import dev.luna5ama.kmogus.Ptr
import org.lwjgl.opengl.GL45C

/* Buffer & Map */
fun glNamedBufferStorage(buffer: Int, size: Long, data: Ptr, flag: Int) {
    GL45C.nglNamedBufferStorage(buffer, size, data.address, flag)
}

fun glNamedBufferStorage(buffer: Int, size: Long, data: Long, flag: Int) {
    GL45C.nglNamedBufferStorage(buffer, size, data, flag)
}

fun glMapNamedBufferRange(buffer: Int, offset: Long, length: Long, access: Int): Arr {
    return Arr.wrap(GL45C.nglMapNamedBufferRange(buffer, offset, length, access), length)
}
