package dev.exceptionteam.sakura.graphics.gl.shader

import com.mojang.blaze3d.systems.RenderSystem
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL45
import java.nio.FloatBuffer

class PosColorShader3D: Shader(
    vertShaderPath = "/assets/sakura/shader/general/PosColor3D.vert",
    fragShaderPath = "/assets/sakura/shader/general/PosColor.frag"
) {

    private val buffer: FloatBuffer = BufferUtils.createFloatBuffer(4 * 4)

    private val projUniform = glGetUniformLocation(id, "u_Proj")
    private val modelViewUniform = glGetUniformLocation(id, "u_ModelView")

    fun default() {
        set(projUniform, RenderSystem.getProjectionMatrix())
        set(modelViewUniform, RenderSystem.getModelViewStack())
    }

    private fun set(location: Int, mat: Matrix4f) {
        mat.get(buffer)
        GL45.glUniformMatrix4fv(location, false, buffer)
    }

}