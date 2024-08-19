package dev.exceptionteam.sakura.graphics.shader

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL45
import java.nio.FloatBuffer

class PosColorShader3D: Shader(
    vertShaderPath = "/assets/sakura/shader/general/PosColor3D.vert",
    fragShaderPath = "/assets/sakura/shader/general/PosColor.frag"
) {

    companion object {
        private var matrix: Matrix4f = Matrix4f()

        fun setMatrix(mat: Matrix4f) {
            matrix = mat
        }
    }

    private val buffer: FloatBuffer = BufferUtils.createFloatBuffer(4 * 4)

    private val matrixUniform = glGetUniformLocation(id, "matrix")

    fun default() {
        set(matrixUniform, matrix)
    }

    private fun set(location: Int, mat: Matrix4f) {
        mat.get(buffer)
        GL45.glUniformMatrix4fv(location, false, buffer)
    }

}