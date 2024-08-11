package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.gl.buffer.PersistentMappedVBO
import dev.exceptionteam.sakura.graphics.gl.glBindVertexArray
import dev.exceptionteam.sakura.graphics.gl.glDrawArrays
import dev.exceptionteam.sakura.graphics.gl.shader.Shader
import dev.exceptionteam.sakura.utils.math.vector.Vec2f
import dev.exceptionteam.sakura.utils.resources.Resource
import org.lwjgl.opengl.GL45

object Render2DUtils {

    private var drawCount = 0

    private val shader2d = Shader(
        Resource("/assets/sakura/shader/general/Render2D.vert"),
        Resource("/assets/sakura/shader/general/Render2D.frag")
    )

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        putVertex2D(x, y, color)
        putVertex2D(x + width, y, color)
        putVertex2D(x + width, y + height, color)
        putVertex2D(x, y, color)
        putVertex2D(x, y + height, color)
        putVertex2D(x + width, y + height, color)

        draw(GL45.GL_TRIANGLES)
    }

    fun putVertex2D(pos: Vec2f, color: ColorRGB) {
        putVertex2D(pos.x, pos.y, color)
    }

    fun putVertex2D(x: Float, y: Float, color: ColorRGB) {
        val arr = PersistentMappedVBO.arr
        val pointer = arr.ptr
        pointer[0] = x
        pointer[4] = y
        pointer[8] = color.rgba
        arr += 12
        ++drawCount
    }

    fun draw(mode: Int) {
        if (drawCount <= 0) return

        begin()

//        shader2d.bind()
//        shader2d.setDefaults()
        glBindVertexArray(PersistentMappedVBO.VAO_2D)
        glDrawArrays(mode, PersistentMappedVBO.offset.toInt(), drawCount)
        PersistentMappedVBO.end()
        glBindVertexArray(0)

        drawCount = 0
        end()
    }

    private fun begin() {
        GL45.glEnable(GL45.GL_BLEND)
    }

    private fun end() {
        GL45.glDisable(GL45.GL_BLEND)
    }

}