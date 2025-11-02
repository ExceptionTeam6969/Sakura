package team.exception.sakura.graphics.gl

import com.mojang.blaze3d.opengl.GlStateManager
import org.lwjgl.opengl.GL11.*
import team.exception.sakura.graphics.geek2.G2GraphicsPipeline

class GlGraphicsPipeline(
    primitive: Primitive,
    override val shaderSet: GlShaderSet,
    pipelineStates: PipelineStates,
    vertexInput: GlVertexInput? = null,
) : G2GraphicsPipeline(primitive, shaderSet, pipelineStates, vertexInput) {

    fun bind() {
        val states = pipelineStates

        if (states.depthTest) GlStateManager._enableDepthTest() else GlStateManager._disableDepthTest()
        GlStateManager._depthMask(states.depthWrite)
        GlStateManager._depthFunc(states.depthFunc.toGl())

        if (states.cullEnable) {
            GlStateManager._enableCull()
            glCullFace(states.cullMode.toGl())
        } else GlStateManager._disableCull()

        glFrontFace(states.frontFace.toGl())
        GlStateManager._polygonMode(GL_FRONT_AND_BACK, states.fillMode.toGl())

        if (states.blendEnable) {
            GlStateManager._enableBlend()
            GlStateManager._blendFuncSeparate(
                states.srcColorBlend.toGl(),
                states.dstColorBlend.toGl(),
                states.srcAlphaBlend.toGl(),
                states.dstAlphaBlend.toGl()
            )
        } else GlStateManager._disableBlend()

        GlStateManager._colorMask(
            states.colorWriteMask.red(),
            states.colorWriteMask.green(),
            states.colorWriteMask.blue(),
            states.colorWriteMask.alpha()
        )

        GlStateManager._glUseProgram(shaderSet.programId)
    }

    companion object {
        fun Primitive.toGL(): Int = when (this) {
            Primitive.TRIANGLES -> GL_TRIANGLES
            Primitive.TRIANGLE_FAN -> GL_TRIANGLE_FAN
            Primitive.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
            Primitive.LINES -> GL_LINES
            Primitive.LINE_STRIP -> GL_LINE_STRIP
            Primitive.POINTS -> GL_POINTS
        }

        fun DepthFunc.toGl() = when (this) {
            DepthFunc.NEVER -> GL_NEVER
            DepthFunc.LESS -> GL_LESS
            DepthFunc.EQUAL -> GL_EQUAL
            DepthFunc.LEQUAL -> GL_LEQUAL
            DepthFunc.GREATER -> GL_GREATER
            DepthFunc.NOTEQUAL -> GL_NOTEQUAL
            DepthFunc.GEQUAL -> GL_GEQUAL
            DepthFunc.ALWAYS -> GL_ALWAYS
        }

        fun CullMode.toGl() = when (this) {
            CullMode.FRONT -> GL_FRONT
            CullMode.BACK -> GL_BACK
            CullMode.FRONT_AND_BACK -> GL_FRONT_AND_BACK
            CullMode.NONE -> 0
        }

        fun FrontFace.toGl() = when (this) {
            FrontFace.CLOCKWISE -> GL_CW
            FrontFace.COUNTER_CLOCKWISE -> GL_CCW
        }

        fun FillMode.toGl() = when (this) {
            FillMode.FILL -> GL_FILL
            FillMode.LINE -> GL_LINE
            FillMode.POINT -> GL_POINT
        }

        fun BlendFunc.toGl() = when (this) {
            BlendFunc.ZERO -> GL_ZERO
            BlendFunc.ONE -> GL_ONE
            BlendFunc.SRC_COLOR -> GL_SRC_COLOR
            BlendFunc.ONE_MINUS_SRC_COLOR -> GL_ONE_MINUS_SRC_COLOR
            BlendFunc.DST_COLOR -> GL_DST_COLOR
            BlendFunc.ONE_MINUS_DST_COLOR -> GL_ONE_MINUS_DST_COLOR
            BlendFunc.SRC_ALPHA -> GL_SRC_ALPHA
            BlendFunc.ONE_MINUS_SRC_ALPHA -> GL_ONE_MINUS_SRC_ALPHA
            BlendFunc.DST_ALPHA -> GL_DST_ALPHA
            BlendFunc.ONE_MINUS_DST_ALPHA -> GL_ONE_MINUS_DST_ALPHA
        }

        fun ColorMask.red() = this == ColorMask.RED || this == ColorMask.ALL
        fun ColorMask.green() = this == ColorMask.GREEN || this == ColorMask.ALL
        fun ColorMask.blue() = this == ColorMask.BLUE || this == ColorMask.ALL
        fun ColorMask.alpha() = this == ColorMask.ALPHA || this == ColorMask.ALL
    }
}
