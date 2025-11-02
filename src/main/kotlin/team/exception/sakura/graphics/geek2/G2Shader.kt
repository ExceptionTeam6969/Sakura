package team.exception.sakura.graphics.geek2

import java.nio.ByteBuffer

/**
 * Warn: This class can only be created by G2Device.
 */
abstract class G2Shader(
    open val device: G2Device,
    val source: ByteBuffer,
    val sourceType: SourceType,
    val entryPoint: String = "main",
) {

    abstract fun compile()

    abstract fun destroy()

    enum class SourceType {
        SPIR_V,
        GLSL,
    }

    enum class ShaderType {
        VERTEX,
        FRAGMENT,
    }

}