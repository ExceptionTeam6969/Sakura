package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.geek2.G2Buffer
import team.exception.sakura.graphics.geek2.G2Device
import team.exception.sakura.graphics.geek2.G2Shader
import java.nio.ByteBuffer

class GlDevice: G2Device() {

    override fun createCommandList(): GlCommandList = GlCommandList()

    override fun createBuffer(
        size: Long,
        access: G2Buffer.Access
    ): G2Buffer = GlBuffer(this, size, access)

    override fun createShader(
        source: ByteBuffer,
        sourceType: G2Shader.SourceType,
        shaderType: G2Shader.ShaderType,
        entryPoint: String
    ): G2Shader = GlShader(this, source, sourceType, shaderType, entryPoint).apply {
        compile()
    }

}