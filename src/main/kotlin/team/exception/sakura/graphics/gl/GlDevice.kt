package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.geek2.G2Buffer
import team.exception.sakura.graphics.geek2.G2Device
import team.exception.sakura.graphics.geek2.G2Shader
import team.exception.sakura.graphics.geek2.G2ShaderSet
import java.nio.ByteBuffer

class GlDevice: G2Device() {

    override fun createCommandList(): GlCommandList = GlCommandList()

    override fun createBuffer(
        size: Long,
        access: G2Buffer.Access
    ): GlBuffer = GlBuffer(this, size, access)

    override fun createShader(
        source: ByteBuffer,
        sourceType: G2Shader.SourceType,
        shaderType: G2Shader.ShaderType,
        entryPoint: String
    ): GlShader = GlShader(this, source, sourceType, shaderType, entryPoint).apply {
        compile()
    }

    override fun createShaderSet(shaders: List<G2Shader>): GlShaderSet = GlShaderSet(
        this, shaders.map { it as GlShader }
    ).apply { attachShaders() }

}