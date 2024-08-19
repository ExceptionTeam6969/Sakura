package dev.exceptionteam.sakura.graphics.shader

import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL45
import java.nio.FloatBuffer

class PosColorShader2D: Shader(
    vertShaderPath = "/assets/sakura/shader/general/PosColor2D.vert",
    fragShaderPath = "/assets/sakura/shader/general/PosColor.frag"
) {

    private val buffer: FloatBuffer = BufferUtils.createFloatBuffer(4 * 4)

    private val matrixUniform = glGetUniformLocation(id, "matrix")

    fun default() {
        set(matrixUniform, MatrixStack.peek().mvpMatrix)
    }

    private fun set(location: Int, mat: Matrix4f) {
        mat.get(buffer)
        GL45.glUniformMatrix4fv(location, false, buffer)
    }

}