package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.geek2.G2Shader
import org.lwjgl.opengl.GL41.*
import org.lwjgl.opengl.GL46.GL_SHADER_BINARY_FORMAT_SPIR_V
import org.lwjgl.opengl.GL46.glSpecializeShader
import team.exception.sakura.graphics.G2RenderSystem
import team.exception.sakura.utils.extension.ByteBufferUtils.readAsString
import team.exception.sakura.utils.extension.StringUtils.asCharSequence
import java.nio.ByteBuffer

/**
 * SPIR-V is not supported in OpenGL 4.1 (macOS)
 */
class GlShader(
    override val device: GlDevice,
    source: ByteBuffer,
    sourceType: SourceType,
    shaderType: ShaderType,
    entryPoint: String = "main",
): G2Shader(device, source, sourceType, entryPoint) {

    init {
        if (sourceType == SourceType.SPIR_V &&
            G2RenderSystem.gpuType == G2RenderSystem.GpuTypes.APPLE) {
            throw Exception("SPIR-V is not supported in OpenGL 4.1 (macOS)")
        }
    }

    val shaderId: Int = glCreateShader(shaderType.toGl())

    override fun compile() {
        val cmdList = device.createCommandList()

        cmdList.add {
            when (sourceType) {
                SourceType.SPIR_V -> {
                    glShaderBinary(
                        intArrayOf(shaderId),
                        GL_SHADER_BINARY_FORMAT_SPIR_V,
                        source
                    )
                    glSpecializeShader(
                        shaderId, entryPoint.asCharSequence(),
                        null as IntArray?, null as IntArray?
                    )
                }
                SourceType.GLSL -> glShaderSource(shaderId, source.readAsString())
            }
            glCompileShader(shaderId)
        }
        cmdList.summitAndDestroy()

        val compileStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS)
        if (compileStatus == GL_FALSE) {
            val infoLog = glGetShaderInfoLog(shaderId)
            throw Exception("Shader compilation failed: $infoLog")
        }
    }

    override fun destroy() {
        glDeleteShader(shaderId)
    }

    companion object {
        fun ShaderType.toGl() = when (this) {
            ShaderType.VERTEX -> GL_VERTEX_SHADER
            ShaderType.FRAGMENT -> GL_FRAGMENT_SHADER
        }
    }
}