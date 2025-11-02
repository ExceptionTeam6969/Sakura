package team.exception.sakura.graphics.geek2

abstract class G2GraphicsPipeline(
    val primitive: Primitive,
    open val shaderSet: G2ShaderSet,
    val pipelineStates: PipelineStates,
    val vertexInput: G2VertexInput? = null,
) {

    data class PipelineStates(
        var depthTest: Boolean = false,
        var depthWrite: Boolean = true,
        var depthFunc: DepthFunc = DepthFunc.LESS,
        var cullEnable: Boolean = false,
        var cullMode: CullMode = CullMode.BACK,
        var frontFace: FrontFace = FrontFace.COUNTER_CLOCKWISE,
        var fillMode: FillMode = FillMode.FILL,
        var blendEnable: Boolean = false,
        var blendEquation: BlendEquation = BlendEquation.ADD,
        var srcColorBlend: BlendFunc = BlendFunc.SRC_ALPHA,
        var dstColorBlend: BlendFunc = BlendFunc.ONE_MINUS_SRC_ALPHA,
        var srcAlphaBlend: BlendFunc = BlendFunc.ONE,
        var dstAlphaBlend: BlendFunc = BlendFunc.ZERO,
        var colorWriteMask: ColorMask = ColorMask.ALL
    )

    enum class Primitive {
        TRIANGLES,
        TRIANGLE_FAN,
        TRIANGLE_STRIP,
        LINES,
        LINE_STRIP,
        POINTS
    }

    enum class CullMode {
        NONE,
        FRONT,
        BACK,
        FRONT_AND_BACK
    }

    enum class FillMode {
        FILL,
        LINE,
        POINT
    }

    enum class FrontFace {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    enum class DepthFunc {
        NEVER,
        LESS,
        EQUAL,
        LEQUAL,
        GREATER,
        NOTEQUAL,
        GEQUAL,
        ALWAYS
    }

    enum class BlendEquation {
        ADD,
        SUBTRACT,
        REVERSE_SUBTRACT,
        MIN,
        MAX
    }

    enum class BlendFunc {
        ZERO,
        ONE,
        SRC_COLOR,
        ONE_MINUS_SRC_COLOR,
        DST_COLOR,
        ONE_MINUS_DST_COLOR,
        SRC_ALPHA,
        ONE_MINUS_SRC_ALPHA,
        DST_ALPHA,
        ONE_MINUS_DST_ALPHA,
    }

    enum class ColorMask {
        NONE,
        RED,
        GREEN,
        BLUE,
        ALPHA,
        ALL
    }
}
