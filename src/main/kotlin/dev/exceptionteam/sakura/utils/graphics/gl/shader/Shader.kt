package dev.exceptionteam.sakura.utils.graphics.gl.shader

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.utils.graphics.gl.GlObject
import dev.exceptionteam.sakura.utils.resources.Resource
import org.lwjgl.opengl.GL45.*

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
        if (vertErr != "") Sakura.logger.error(vertErr)

        glShaderSource(fragment, frag.getString())
        glCompileShader(fragment)
        val fragErr = glGetShaderInfoLog(vertex)
        if (fragErr != "") Sakura.logger.error(fragErr)

        // 创建着色器程序
        id = glCreateProgram()
        glAttachShader(id, vertex)
        glAttachShader(id, fragment)
        glLinkProgram(id)
        val progErr = glGetProgramInfoLog(id)
        if (progErr != "") Sakura.logger.error(progErr)

        // 删除着色器 没用了已经
        glDeleteShader(vertex)
        glDeleteShader(fragment)
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

}