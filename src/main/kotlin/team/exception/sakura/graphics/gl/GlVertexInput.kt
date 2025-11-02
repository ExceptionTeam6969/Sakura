package team.exception.sakura.graphics.gl

import org.lwjgl.opengl.GL33.*
import team.exception.sakura.graphics.geek2.G2VertexInput

class GlVertexInput: G2VertexInput() {

    private val vaoId: Int = glGenVertexArrays()

    private var offset = 0L

    var stride: Int = 0

    fun bind() {
        glBindVertexArray(vaoId)
    }

    fun unbind() {
        glBindVertexArray(0)
    }

    override fun int(index: Int) {
        glEnableVertexAttribArray(index)
        glVertexAttribIPointer(index, 1, GL_INT, stride, offset)
        offset += Int.SIZE_BYTES
    }

    override fun float(index: Int) {
        glEnableVertexAttribArray(index)
        glVertexAttribPointer(index, 1, GL_FLOAT, false, stride, offset)
        offset += Float.SIZE_BYTES
    }

    override fun vec2(index: Int) {
        glEnableVertexAttribArray(index)
        glVertexAttribPointer(index, 2, GL_FLOAT, false, stride, offset)
        offset += 2 * Float.SIZE_BYTES
    }

    override fun vec3(index: Int) {
        glEnableVertexAttribArray(index)
        glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, offset)
        offset += 3 * Float.SIZE_BYTES
    }

    override fun vec4(index: Int) {
        glEnableVertexAttribArray(index)
        glVertexAttribPointer(index, 4, GL_FLOAT, false, stride, offset)
        offset += 4 * Float.SIZE_BYTES
    }

    override fun mat2(index: Int) {
        for (i in 0 until 2) {
            glEnableVertexAttribArray(index + i)
            glVertexAttribPointer(index + i, 2, GL_FLOAT, false, stride, offset + i * 2 * Float.SIZE_BYTES)
            glVertexAttribDivisor(index + i, 1)
        }
        offset += 4 * Float.SIZE_BYTES
    }

    override fun mat3(index: Int) {
        for (i in 0 until 3) {
            glEnableVertexAttribArray(index + i)
            glVertexAttribPointer(index + i, 3, GL_FLOAT, false, stride, offset + i * 3 * Float.SIZE_BYTES)
            glVertexAttribDivisor(index + i, 1)
        }
        offset += 9 * Float.SIZE_BYTES
    }

    /**
     * Enables a mat4 attribute (4 columns of vec4).
     * Each column is treated as a separate attribute.
     */
    override fun mat4(index: Int) {
        for (i in 0 until 4) {
            glEnableVertexAttribArray(index + i)
            glVertexAttribPointer(index + i, 4, GL_FLOAT, false, stride, offset + i * 4 * Float.SIZE_BYTES)
            glVertexAttribDivisor(index + i, 1)
        }
        offset += 16 * Float.SIZE_BYTES
    }

    override fun destroy() {
        glDeleteVertexArrays(vaoId)
    }
}
