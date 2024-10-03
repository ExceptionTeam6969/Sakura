package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45.*

object RenderUtilsTexture {

    fun drawTextureRect(
        x: Float, y: Float,
        width: Float, height: Float,
        texture: Texture,
        color: ColorRGB = ColorRGB.BLACK
    ) {
        val endX = x + width
        val endY = y + height

        texture.use {
            GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
                texture(endX, y, 1f, 0f, color)
                texture(x, y, 0f, 0f, color)
                texture(endX, endY, 1f, 1f, color)
                texture(x, endY, 0f, 1f, color)
            }
        }
    }

}