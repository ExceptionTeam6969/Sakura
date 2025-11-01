package team.exception.sakura.graphics.gl

import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.geek2.G2GraphicsPipeline
import team.exception.sakura.graphics.geek2.G2ShaderSet

class GlGraphicsPipeline(
    primitive: Primitive,
    shaderSet: G2ShaderSet
): G2GraphicsPipeline(primitive, shaderSet) {

    companion object {
        fun Primitive.toGlPrimitive(): Int = when (this) {
            Primitive.TRIANGLES -> GL_TRIANGLES
            Primitive.TRIANGLE_FAN -> GL_TRIANGLE_FAN
            Primitive.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
            Primitive.LINES -> GL_LINES
            Primitive.LINE_STRIP -> GL_LINE_STRIP
            Primitive.POINTS -> GL_POINTS
        }
    }

}