package dev.exceptionteam.sakura.graphics.gl

import org.lwjgl.opengl.GL45

enum class GlDataType(val glEnum: Int, val size: Int) {
    GL_BYTE(GL45.GL_BYTE, 1),
    GL_UNSIGNED_BYTE(GL45.GL_UNSIGNED_BYTE, 1),
    GL_SHORT(GL45.GL_SHORT, 2),
    GL_UNSIGNED_SHORT(GL45.GL_UNSIGNED_SHORT, 2),
    GL_INT(GL45.GL_INT, 4),
    GL_UNSIGNED_INT(GL45.GL_UNSIGNED_INT, 4),
    GL_FLOAT(GL45.GL_FLOAT, 4),
}