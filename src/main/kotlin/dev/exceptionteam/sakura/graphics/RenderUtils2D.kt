package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45

object RenderUtils2D {

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosColor2D) {
            vertex(endX, startY, color)
            vertex(startX, startY, color)
            vertex(endX, endY, color)
            vertex(startX, endY, color)
        }
    }

    fun drawRectGradientH(x: Float, y: Float, width: Float, height: Float, startColor: ColorRGB, endColor: ColorRGB) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosColor2D) {
            vertex(endX, startY, endColor)
            vertex(startX, startY, startColor)
            vertex(endX, endY, endColor)
            vertex(startX, endY, startColor)
        }
    }

    fun drawRectGradientV(x: Float, y: Float, width: Float, height: Float, startColor: ColorRGB, endColor: ColorRGB) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosColor2D) {
            vertex(endX, startY, startColor)
            vertex(startX, startY, startColor)
            vertex(endX, endY, endColor)
            vertex(startX, endY, endColor)
        }
    }

    fun drawRectOutline(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        GL45.glEnable(GL45.GL_LINE_SMOOTH)
        GL45.GL_LINE_LOOP.draw(VertexBufferObjects.PosColor2D) {
            vertex(startX, startY, color)
            vertex(endX, startY, color)
            vertex(endX, endY, color)
            vertex(startX, endY, color)
        }
        GL45.glDisable(GL45.GL_LINE_SMOOTH)
    }

    fun drawLineNoSmoothH(x: Float, y: Float, width: Float, color: ColorRGB) {
        drawLineNoSmooth(x, y, x + width, y, color)
    }

    fun drawLineNoSmooth(x1: Float, y1: Float, x2: Float, y2: Float, color: ColorRGB) {
        GL45.glDisable(GL45.GL_LINE_SMOOTH)
        GL45.glLineWidth(1f)
        GL45.GL_LINES.draw(VertexBufferObjects.PosColor2D) {
            vertex(x1, y1, color)
            vertex(x2, y2, color)
        }
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, color: ColorRGB) {
        GL45.glEnable(GL45.GL_LINE_SMOOTH)
        GL45.glLineWidth(1f)
        GL45.GL_LINES.draw(VertexBufferObjects.PosColor2D) {
            vertex(x1, y1, color)
            vertex(x2, y2, color)
        }
        GL45.glDisable(GL45.GL_LINE_SMOOTH)
    }

    fun drawTextureRect(
        x: Float, y: Float, width: Float, height: Float,
        texture: Texture, color: ColorRGB = ColorRGB.WHITE
    ) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        GL45.glEnable(GL45.GL_BLEND)
        GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA)

        texture.use {
            GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
                texture(endX, startY, 1f, 0f, color)
                texture(startX, startY, 0f, 0f,color)
                texture(endX, endY, 1f, 1f,color)
                texture(startX, endY, 0f, 1f, color)
            }
        }
    }

}