package dev.exceptionteam.sakura.graphics.shader

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.GlHelper
import dev.exceptionteam.sakura.graphics.GlObject
import dev.exceptionteam.sakura.graphics.shader.impl.*
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.io.InputStream
import java.io.StringWriter
import java.nio.FloatBuffer
import java.nio.charset.Charset
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

open class Shader(vertShaderPath: String, fragShaderPath: String) : GlObject {
    private val buffer: FloatBuffer = BufferUtils.createFloatBuffer(4 * 4)

    final override var id: Int

    init {
        val vertexShaderID = createShader(vertShaderPath, GL_VERTEX_SHADER)
        val fragShaderID = createShader(fragShaderPath, GL_FRAGMENT_SHADER)
        val id = glCreateProgram()

        glAttachShader(id, vertexShaderID)
        glAttachShader(id, fragShaderID)

        glLinkProgram(id)
        val linked = glGetProgrami(id, GL_LINK_STATUS)
        if (linked == 0) {
            Sakura.logger.error(glGetShaderInfoLog(id, 1024))
            glDeleteProgram(id)
            throw ShaderCompileException("Failed to link shader: $vertShaderPath, $fragShaderPath")
        }
        this.id = id

        glDetachShader(id, vertexShaderID)
        glDetachShader(id, fragShaderID)
        glDeleteShader(vertexShaderID)
        glDeleteShader(fragShaderID)
    }

    private fun createShader(path: String, shaderType: Int): Int {
        val srcString = javaClass.getResourceAsStream(path)!!.use { it.readText() }
        val id = glCreateShader(shaderType)

        glShaderSource(id, srcString)
        glCompileShader(id)

        val compiled = glGetShaderi(id, GL_COMPILE_STATUS)
        if (compiled == 0) {
            Sakura.logger.error("$path\n" + glGetShaderInfoLog(id, 1024))
            glDeleteShader(id)
            throw ShaderCompileException("Failed to compile shader: $path")
        }

        return id
    }

    override fun bind() {
        GlHelper.program = id
    }

    override fun unbind() {
        GlHelper.program = 0
    }

    override fun delete() {
        glDeleteProgram(id)
    }

    open fun default() {}

    /* uniform functions */

    // matrix4f
    protected fun set(location: Int, mat: Matrix4f) {
        mat.get(buffer)
        glProgramUniformMatrix4fv(id, location, false, buffer)
    }

    // sampler2D
    protected fun set(location: Int, textureUnit: Int) {
        glProgramUniform1i(id, location, textureUnit)
    }

    class ShaderCompileException(message: String): Exception(message)

}

fun initShaders() {
    PosColorShader2D
    PosTexShader2D
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Shader> T.use(block: T.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    bind()
    block.invoke(this)
    glUseProgram(0)
}

fun InputStream.readText(charset: Charset = Charsets.UTF_8, bufferSize: Int = DEFAULT_BUFFER_SIZE): String {
    val stringWriter = StringWriter()
    buffered(bufferSize / 2).reader(charset).copyTo(stringWriter, bufferSize / 2)
    return stringWriter.toString()
}