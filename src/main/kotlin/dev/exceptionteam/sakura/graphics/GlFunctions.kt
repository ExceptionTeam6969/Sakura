package dev.exceptionteam.sakura.graphics

import org.lwjgl.opengl.GL45C

/* Buffer & Map */
fun glNamedBufferStorage(buffer: Int, size: Long, data: Long, flag: Int) {
    GL45C.nglNamedBufferStorage(buffer, size, data, flag)
}
