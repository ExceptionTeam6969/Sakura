package team.exception.sakura.graphics.geek2

import java.nio.ByteBuffer

abstract class G2Device {

    /**
     * Create a new command list.
     *
     * Note: You should destroy the command list after use.
     * @return A new command list.
     */
    abstract fun createCommandList(): G2CommandList

    abstract fun createBuffer(
        size: Long,
        access: G2Buffer.Access = G2Buffer.Access.READ_WRITE
    ): G2Buffer

    abstract fun createShader(
        source: ByteBuffer,
        sourceType: G2Shader.SourceType,
        shaderType: G2Shader.ShaderType,
        entryPoint: String
    ): G2Shader

}