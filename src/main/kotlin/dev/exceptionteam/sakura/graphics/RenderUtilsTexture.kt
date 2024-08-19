package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.shader.FontRendererShader
import org.lwjgl.opengl.GL45

object RenderUtilsTexture {

    private val shader = FontRendererShader()
    private var vertexSize = 0

    fun drawTextureRect(
        x: Float, y: Float,
        width: Float, height: Float,
        color: ColorRGB = ColorRGB.WHITE
    ) {
        putVertex(x + width, y, 0f, 1f, color)
        putVertex(x, y, 1f, 1f, color)
        putVertex(x + width, y + height, 1f, 0f, color)
        putVertex(x, y + height, 0f, 0f, color)

        draw(GL45.GL_TRIANGLE_STRIP)
    }

    private fun putVertex(x: Float, y: Float, u: Float, v: Float, color: ColorRGB) {
        val arr = PMBuffer.arr
        val ptr = arr.ptr

        ptr[0] = x
        ptr[4] = y
        ptr[8] = u
        ptr[12] = v
        ptr[16] = color.rgba

        arr += 20
        vertexSize++
    }

    private fun draw(mode: Int) {
        if (vertexSize <= 0) return
        shader.bind()
    }

}