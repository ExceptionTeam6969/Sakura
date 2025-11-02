package team.exception.sakura.graphics.geek2

/**
 * Note: This class can only be created by G2Device.
 */
abstract class G2ShaderSet(
    open val device: G2Device,
    open val shaders: List<G2Shader>,
) {

    abstract fun attachShaders()

}