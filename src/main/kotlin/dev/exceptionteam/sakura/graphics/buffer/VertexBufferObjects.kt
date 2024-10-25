package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlDataType
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.impl.*
import dev.exceptionteam.sakura.graphics.shader.Shader
import org.lwjgl.opengl.GL45.*

object VertexBufferObjects {

    fun sync() {
        values.forEach { it.vbo.onSync() }
    }

    private val values = listOf(PosColor2D, PosTex2D)

    data object PosColor2D: VertexMode(
        PosColorShader2D, buildAttribute(12) {
            float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    )

    data object PosTex2D: VertexMode(
        PosTexShader2D, buildAttribute(20) {
            float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(1, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(2, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    )

    open class VertexMode(val shader: Shader, private val attribute: VertexAttribute) {

        val vbo = PersistentMappedVBO(attribute.stride)
        private val vao = createVao(vbo, attribute)

        private val arr get() = vbo.arr
        private var vertexSize = 0

        fun vertex(x: Float, y: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, 0f)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }

        fun texture(x: Float, y: Float, u: Float, v: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, 0f)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = u
            pointer[12] = v
            pointer[16] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }

        fun draw(vertexMode: VertexMode, shader: Shader, mode: Int) {
            if (vertexSize == 0) return
            shader.bind()
            shader.default()
            glBindVertexArray(vertexMode.vao)
            glDrawArrays(mode, vertexMode.vbo.offset.toInt(), vertexSize)
            vbo.end()
            glBindVertexArray(0)
            vertexSize = 0
        }

    }

    /* Vertex Array Object */
    private fun createVao(vbo: PersistentMappedVBO, vertexAttribute: VertexAttribute): Int {
        val vaoID = glCreateVertexArrays()
        glBindVertexArray(vaoID)
        glBindBuffer(GL_ARRAY_BUFFER, vbo.id)
        vertexAttribute.apply()
//        glBindVertexArray(0)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return vaoID
    }

    fun Int.draw(vertexMode: VertexMode, shader: Shader = vertexMode.shader, block: VertexMode.() -> Unit) {
        vertexMode.block()
        vertexMode.draw(vertexMode, shader, this)
    }

}