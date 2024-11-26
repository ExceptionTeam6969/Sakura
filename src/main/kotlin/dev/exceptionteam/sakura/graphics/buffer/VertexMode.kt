package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlHelper
import dev.exceptionteam.sakura.graphics.shader.Shader
import org.lwjgl.opengl.GL45.*

open class VertexMode(val shader: Shader, protected val attribute: VertexAttribute) {
    val vbo = PersistentMappedVBO(attribute.stride)
    private val vao = createVao(vbo, attribute)

    protected val arr get() = vbo.arr
    protected var vertexSize = 0

    fun draw(shader: Shader, mode: Int) {
        if (vertexSize == 0) return
        shader.bind()
        shader.default()
        GlHelper.vertexArray = this.vao
        glDrawArrays(mode, this.vbo.offset.toInt(), vertexSize)
        vbo.end()
        vertexSize = 0
    }

    /* Vertex Array Object */
    private fun createVao(vbo: PersistentMappedVBO, vertexAttribute: VertexAttribute): Int {
        val vaoID = glCreateVertexArrays()
        GlHelper.vertexArray = vaoID
        glBindBuffer(GL_ARRAY_BUFFER, vbo.id)
        vertexAttribute.apply()
        GlHelper.vertexArray = 0
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return vaoID
    }
}