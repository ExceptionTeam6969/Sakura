package team.exception.sakura.graphics.buffer

abstract class G2GraphicsPipeline(
    val primitive: Primitive,
    val shaderSet: G2ShaderSet
) {

    enum class Primitive {
        TRIANGLES,
        TRIANGLE_FAN,
        TRIANGLE_STRIP,
        LINES,
        LINE_STRIP,
        POINTS
    }

}