package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import org.lwjgl.opengl.GL45

object RenderUtils2D {

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosColor2D) {
            vertex(x + width, y, color)
            vertex(x, y, color)
            vertex(x + width, y + height, color)
            vertex(x, y + height, color)
        }
    }

    fun drawRectOutline(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        GL45.glEnable(GL45.GL_LINE_SMOOTH)
        GL45.GL_LINE_LOOP.draw(VertexBufferObjects.PosColor2D) {
            vertex(x, y, color)
            vertex(x + width, y, color)
            vertex(x + width, y + height, color)
            vertex(x, y + height, color)
        }
        GL45.glDisable(GL45.GL_LINE_SMOOTH)
    }

}