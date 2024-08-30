package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.texture.Texture
import org.lwjgl.opengl.GL45

object RenderUtilsTexture {

    fun drawTextureRect(
        x: Float, y: Float,
        width: Float, height: Float,
        texture: Texture,
        color: ColorRGB = ColorRGB.WHITE
    ) {
        GL45.glEnable(GL45.GL_BLEND)
        GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA)

        val endX = x + width
        val endY = y + height
        texture.use {
            GL45.GL_TRIANGLE_STRIP.draw(VertexBufferObjects.PosTex2D) {
                vertex(x, endY, 0f, 1f, color)
                vertex(endX, endY, 1f, 1f, color)
                vertex(x, y, 0f, 0f, color)
                vertex(endX, y, 1f, 0f, color)
            }
        }
    }

}