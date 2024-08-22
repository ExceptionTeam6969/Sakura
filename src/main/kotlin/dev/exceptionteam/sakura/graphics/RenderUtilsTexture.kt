package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.FontRendererShader
import org.lwjgl.opengl.GL45

object RenderUtilsTexture {

    private var vertexSize = 0

    fun drawTextureRect(
        x: Float, y: Float,
        width: Float, height: Float,
        texture: Int,
        color: ColorRGB = ColorRGB.WHITE
    ) {
        putVertex(x + width, y, 1f, 0f, color)
        putVertex(x, y, 0f, 0f, color)
        putVertex(x + width, y + height, 1f, 1f, color)
        putVertex(x, y + height, 0f, 1f, color)

        draw(GL45.GL_TRIANGLE_STRIP, texture)
    }

    private fun putVertex(x: Float, y: Float, u: Float, v: Float, color: ColorRGB) {
        val position = MatrixStack.getPosition(x, y, 0f)
        val arr = PMBuffer.arr
        val ptr = arr.ptr
        ptr[0] = position.x
        ptr[4] = position.y
        ptr[8] = u
        ptr[12] = v
        ptr[16] = color.rgba
        arr += 20
        vertexSize++
    }

    /**
     * @param texture 纹理 为零需手动绑定
     */
    @Suppress("SameParameterValue")
    private fun draw(mode: Int, texture: Int = 0) {
        if (vertexSize <= 0) return
        FontRendererShader.bind()
        FontRendererShader.default()

        if (texture != 0) GL45.glBindTexture(GL45.GL_TEXTURE_2D, texture)
        GL45.glBindVertexArray(FontRendererShader.vao)
        GL45.glDrawArrays(mode, PMBuffer.offset.toInt(), vertexSize)
        PMBuffer.end(20)
        GL45.glBindVertexArray(0)
        if (texture != 0) GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0)

        vertexSize = 0
    }

}