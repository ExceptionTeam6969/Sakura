package dev.exceptionteam.sakura.graphics.gl.shader

import com.mojang.blaze3d.systems.RenderSystem
import dev.exceptionteam.sakura.features.modules.impl.client.RenderSystemMod
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL45
import java.nio.FloatBuffer

class PosColorShader2D: Shader(
    vertShaderPath = "/assets/sakura/shader/general/PosColor2D.vert",
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