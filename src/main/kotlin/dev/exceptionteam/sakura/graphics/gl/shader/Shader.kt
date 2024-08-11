package dev.exceptionteam.sakura.graphics.gl.shader

import com.mojang.blaze3d.systems.RenderSystem
import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.gl.GlObject
import dev.exceptionteam.sakura.utils.resources.Resource
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.nio.FloatBuffer

class Shader(
    vert: Resource,
    frag: Resource,
): GlObject {

    private val vertex = glCreateShader(GL_VERTEX_SHADER)
    private val fragment = glCreateShader(GL_FRAGMENT_SHADER)
    override val id: Int

    init {
        // 编译着色器
        glShaderSource(vertex, vert.getString())
        glCompileShader(vertex)
        val vertErr = glGetShaderInfoLog(vertex)
        if (vertErr.isNotEmpty()) Sakura.logger.error(vertErr)

        glShaderSource(fragment, frag.getString())
        glCompileShader(fragment)
        val fragErr = glGetShaderInfoLog(vertex)
        if (fragErr.isNotEmpty()) Sakura.logger.error(fragErr)

        // 创建着色器程序
        id = glCreateProgram()
        glAttachShader(id, vertex)
        glAttachShader(id, fragment)
        glLinkProgram(id)
        val progErr = glGetProgramInfoLog(id)
        if (progErr.isNotEmpty()) Sakura.logger.error(progErr)

        // 删除着色器 没用了已经
        glDeleteShader(vertex)
        glDeleteShader(fragment)
    }

    private fun getUniformId(name: CharSequence): Int {
        return glGetUniformLocation(id, name)
    }

    fun setUniform(name: CharSequence, value: Int) {
        glUniform1i(getUniformId(name), value)
    }

    fun setUniform(name: CharSequence, value: Boolean) {
        glUniform1i(getUniformId(name), if (value) GL_TRUE else GL_FALSE)
    }

    fun setUniform(name: CharSequence, value: Float) {
        glUniform1f(getUniformId(name), value)
    }

    fun setUniform(name: CharSequence, x: Float, y: Float) {
        glUniform2f(getUniformId(name), x, y)
    }

    fun setUniform(name: CharSequence, mat: Matrix4f) {
        val buf = BufferUtils.createFloatBuffer(4 * 4)
        mat.get(buf)
        glUniformMatrix4fv(getUniformId(name), false, buf)
    }

    override fun bind() {
        glUseProgram(id)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun destroy() {
        glDeleteProgram(id)
    }

    fun setDefaults() {
        setUniform("u_Proj", RenderSystem.getProjectionMatrix())
        setUniform("u_ModelView", RenderSystem.getModelViewStack())
    }

}