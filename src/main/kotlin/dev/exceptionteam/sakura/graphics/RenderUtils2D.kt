package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45
import kotlin.math.cos
import kotlin.math.sin

object RenderUtils2D {

    fun drawDynamicIsland(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radius: Float,
        segments: Int,
        color: ColorRGB,
        rectFilled: Boolean = false
    ) {

        // 绘制左侧半圆
        GL45.GL_TRIANGLE_FAN.draw(VertexBufferObjects.PosColor2D) {
            vertex(x + radius, y + height / 2, color) // 左半圆的中心点
            for (i in segments / 2 downTo -segments / 2) { // 从 180° -> 0° 绘制左半圆
                val theta = Math.PI * i / segments
                val px = (x + radius - radius * cos(theta)).toFloat()
                val py = (y + height / 2 + radius * sin(theta)).toFloat()
                vertex(px, py, color)
            }
        }

        // 绘制右侧半圆
        GL45.GL_TRIANGLE_FAN.draw(VertexBufferObjects.PosColor2D) {
            vertex(x + width - radius, y + height / 2, color) // 右半圆的中心点
            for (i in -segments / 2..segments / 2) { // 从 0° -> 180° 绘制右半圆
                val theta = Math.PI * i / segments
                val px = (x + width - radius + radius * cos(theta)).toFloat()
                val py = (y + height / 2 + radius * sin(theta)).toFloat()
                vertex(px, py, color)
            }
        }

        // 绘制中间矩形
        if (rectFilled) {
            GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosColor2D) {
                vertex(x + radius, y, color) // 左上
                vertex(x + radius, y + height, color) // 左下
                vertex(x + width - radius, y, color) // 右上
                vertex(x + width - radius, y + height, color) // 右下
            }
        } else {
            drawLine(x + radius, y, x + width - radius, y, color)
            drawLine(x + radius, y + height, x + width - radius, y + height, color)
        }
    }

    fun drawCircleFilled(centerX: Float, centerY: Float, radius: Float, segments: Int, color: ColorRGB) {
        GL45.GL_TRIANGLE_FAN.draw(VertexBufferObjects.PosColor2D) {
            // 添加圆心顶点
            vertex(centerX, centerY, color)
            // 计算圆周上的顶点
            for (i in 0..segments) {
                val theta = 2.0 * Math.PI * i / segments // 当前角度
                val x = (centerX + radius * cos(theta)).toFloat()
                val y = (centerY + radius * sin(theta)).toFloat()
                vertex(x, y, color)
            }

            // 闭合圆形（最后一个点与第一个点重合）
            vertex(centerX + radius, centerY, color)
        }
    }

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

        GlHelper.lineSmooth = true
        GL45.GL_LINE_LOOP.draw(VertexBufferObjects.PosColor2D) {
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
        GL45.GL_LINES.draw(VertexBufferObjects.PosColor2D) {
            vertex(x1, y1, color)
            vertex(x2, y2, color)
        }
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, color: ColorRGB) {
        GlHelper.lineSmooth = true
        GL45.glLineWidth(1f)
        GL45.GL_LINES.draw(VertexBufferObjects.PosColor2D) {
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
            GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
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
            GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
                texture(endX, startY, endU, startV, color)
                texture(startX, startY, startU, startV, color)
                texture(endX, endY, endU, endV, color)
                texture(startX, endY, startU, endV, color)
            }
        }
    }

}