package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlDataType
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.impl.*
import dev.exceptionteam.sakura.graphics.shader.Shader
import dev.exceptionteam.sakura.utils.math.MathUtils
import dev.exceptionteam.sakura.utils.math.toRadians
import dev.exceptionteam.sakura.utils.math.vector.Vec2f
import kotlin.math.*

object VertexBufferObjects {

    fun sync() {
        values.forEach { it.vbo.onSync() }
    }

    private val values = listOf(
        PosColor2D,
        PosTex2D,
        PosColor3D,
        RenderFont,
    )

    data object PosColor2D: VertexMode(
        PosColorShader2D, buildAttribute(12) {
            float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    ) {
        fun quad(
            x1: Float, y1: Float, color1: ColorRGB,
            x2: Float, y2: Float, color2: ColorRGB
        ) {
            vertex(x1, y2, color1)
            vertex(x2, y2, color1)
            vertex(x2, y1, color2)
            vertex(x1, y1, color2)
        }

        fun rect(
            x: Float, y: Float,
            width: Float, height: Float,
            color: ColorRGB
        ) {
            quad(x, y, color, x + width, y + height, color)
        }

        fun arc(
            centerX: Float,
            centerY: Float,
            radius: Float,
            angleRange: Pair<Float, Float>,
            segments: Int,
            color: ColorRGB
        ) {
            fun calcSegments(segmentsIn: Int, radius: Float, range: Float): Int {
                if (segmentsIn != -0) return segmentsIn
                return max((radius * 0.5 * PI * (range / 360.0)).roundToInt(), 16)
            }

            val center = Vec2f(centerX, centerY)
            val range = max(angleRange.first, angleRange.second) - min(angleRange.first, angleRange.second)
            val seg = calcSegments(segments, radius, range)
            val segAngle = (range / seg.toFloat())

            for (i in 0..seg) {
                val angle = (i * segAngle + angleRange.first).toRadians()
                val unRounded = Vec2f(sin(angle), -cos(angle)).times(radius).plus(center)
                vertex(MathUtils.round(unRounded.x, 8), MathUtils.round(unRounded.y, 8), color)
            }
        }

        fun vertex(x: Float, y: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, 0f)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }
    }

    data object PosTex2D: VertexMode(
        PosTexShader2D, buildAttribute(20) {
            float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(1, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(2, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    ) {
        fun texture(x: Float, y: Float, u: Float, v: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, 0f)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = u
            pointer[12] = v
            pointer[16] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }
    }

    data object PosColor3D: VertexMode(
        PosColorShader3D, buildAttribute(16) {
            float(0, 3, GlDataType.GL_FLOAT, false)         // 12 bytes
            float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    ) {
        fun vertex(x: Float, y: Float, z: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, z)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = position.z
            pointer[12] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }
    }

    data object RenderFont: VertexMode(
        FontShader, buildAttribute(20) {
            float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(1, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
            float(2, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
        }
    ) {
        fun texture(x: Float, y: Float, u: Float, v: Float, color: ColorRGB) {
            val position = MatrixStack.getPosition(x, y, 0f)
            val pointer = arr.ptr
            pointer[0] = position.x
            pointer[4] = position.y
            pointer[8] = u
            pointer[12] = v
            pointer[16] = color.rgba
            arr += attribute.stride.toLong()
            vertexSize++
        }
    }
}

inline fun <reified T: VertexMode> T.draw(mode: Int, shader: Shader = this.shader, block: T.() -> Unit) {
    this.block()
    this.draw(shader, mode)
}
