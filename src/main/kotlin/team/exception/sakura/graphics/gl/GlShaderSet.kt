package team.exception.sakura.graphics.gl

import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.geek2.G2ShaderSet

class GlShaderSet(
    override val device: GlDevice,
    override val shaders: List<GlShader>,
): G2ShaderSet(device, shaders) {

    val programId: Int = glCreateProgram()

    override fun attachShaders() {
        val cmdList = device.createCommandList()

        cmdList.add {
            shaders.forEach { shader ->
                glAttachShader(programId, shader.shaderId)
            }
            glLinkProgram(programId)
        }
        cmdList.summitAndDestroy()

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw Exception("Error linking program: " + glGetProgramInfoLog(programId))
        }
    }

}