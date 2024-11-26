package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45

object RenderUtils2D {

    fun drawDynamicIsland(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radius: Float,
        segments: Int,
        color: ColorRGB,
        filled: Boolean = false
    ) {
        VertexBufferObjects.PosColor2D.draw(if (filled) GL45.GL_TRIANGLE_FAN else GL45.GL_LINE_LOOP) {
            arc(x + radius, y + radius, radius, Pair(270f, 360f), segments, color)
            arc(x + width - radius, y + radius, radius, Pair(0f, 90f), segments, color)
            arc(x + width - radius, y + height - radius, radius, Pair(90f, 180f), segments, color)
            arc(x + radius, y + height - radius, radius, Pair(180f, 270f), segments, color)
        }
    }

    fun drawCircleFilled(centerX: Float, centerY: Float, radius: Float, segments: Int, color: ColorRGB) {
        VertexBufferObjects.PosColor2D.draw(GL45.GL_TRIANGLE_FAN) {
            arc(centerX, centerY, radius, Pair(0f, 360f), segments, color)
        }
    }

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        VertexBufferObjects.PosColor2D.draw(GL45.GL_TRIANGLE_STRIP) {
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

        VertexBufferObjects.PosColor2D.draw(GL45.GL_TRIANGLE_STRIP) {
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

        VertexBufferObjects.PosColor2D.draw(GL45.GL_TRIANGLE_STRIP) {
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

        GlHelper.lineSmooth = true
        VertexBufferObjects.PosColor2D.draw(GL45.GL_LINE_LOOP) {
            vertex(startX, startY, color)
            vertex(endX, startY, color)
            vertex(endX, endY, color)
            vertex(startX, endY, color)
        }
        GlHelper.lineSmooth = false
    }

    fun drawLineNoSmoothH(x: Float, y: Float, width: Float, color: ColorRGB) {
        drawLineNoSmooth(x, y, x + width, y, color)
    }

    fun drawLineNoSmooth(x1: Float, y1: Float, x2: Float, y2: Float, color: ColorRGB) {
        GlHelper.lineSmooth = false
        GL45.glLineWidth(1f)
        VertexBufferObjects.PosColor2D.draw(GL45.GL_LINES) {
            vertex(x1, y1, color)
            vertex(x2, y2, color)
        }
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, color: ColorRGB) {
        GlHelper.lineSmooth = true
        GL45.glLineWidth(1f)
        VertexBufferObjects.PosColor2D.draw(GL45.GL_LINES) {
            vertex(x1, y1, color)
            vertex(x2, y2, color)
        }
        GlHelper.lineSmooth = false
    }

    fun drawTextureRect(
        x: Float, y: Float, width: Float, height: Float, texture: Texture, color: ColorRGB = ColorRGB.WHITE
    ) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        texture.use {
            VertexBufferObjects.PosTex2D.draw(GL45.GL_TRIANGLE_STRIP) {
                texture(endX, startY, 1f, 0f, color)
                texture(startX, startY, 0f, 0f, color)
                texture(endX, endY, 1f, 1f, color)
                texture(startX, endY, 0f, 1f, color)
            }
        }
    }

    fun drawTextureRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        startU: Float,
        startV: Float,
        endU: Float,
        endV: Float,
        texture: Texture,
        color: ColorRGB = ColorRGB.WHITE
    ) {
        val startX = x
        val startY = y
        val endX = x + width
        val endY = y + height

        texture.use {
            VertexBufferObjects.PosTex2D.draw(GL45.GL_TRIANGLE_STRIP) {
                texture(endX, startY, endU, startV, color)
                texture(startX, startY, startU, startV, color)
                texture(endX, endY, endU, endV, color)
                texture(startX, endY, startU, endV, color)
            }
        }
    }

}