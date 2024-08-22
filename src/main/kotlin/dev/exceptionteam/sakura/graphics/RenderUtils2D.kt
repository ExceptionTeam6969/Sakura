package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.PosColorShader2D
import org.lwjgl.opengl.GL45

object RenderUtils2D {

    private var vertexSize = 0

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        putVertex(x + width, y, color)
        putVertex(x, y, color)
        putVertex(x + width, y + height, color)
        putVertex(x, y + height, color)

        draw(GL45.GL_TRIANGLE_STRIP)
    }

    private fun putVertex(x: Float, y: Float, color: ColorRGB) {
        val position = MatrixStack.getPosition(x, y, 0f)
        val ptr = PMBuffer.arr.ptr
        ptr[0] = position.x
        ptr[4] = position.y
        ptr[8] = color.rgba
        PMBuffer.arr += 12
        vertexSize++
    }

    @Suppress("SameParameterValue")
    private fun draw(mode: Int) {
        if (vertexSize <= 0) return
        PosColorShader2D.bind()
        PosColorShader2D.default()
        GL45.glBindVertexArray(PosColorShader2D.vao)
        GL45.glDrawArrays(mode, PMBuffer.offset.toInt(), vertexSize)
        PMBuffer.end(12)
        GL45.glBindVertexArray(0)
        vertexSize = 0
    }

}