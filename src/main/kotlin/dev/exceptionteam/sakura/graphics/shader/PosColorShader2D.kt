package dev.exceptionteam.sakura.graphics.shader

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.GlDataType
import dev.exceptionteam.sakura.graphics.buffer.buildAttribute
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL20.glGetUniformLocation

object PosColorShader2D: Shader(
    vertShaderPath = "${Sakura.ASSETS_DIRECTORY}/shader/general/PosColor2D.vert",
    fragShaderPath = "${Sakura.ASSETS_DIRECTORY}/shader/general/PosColor.frag"
) {

    private val matrixLocation = glGetUniformLocation(id, "matrix")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
    }

    val vao = createVao(buildAttribute(12) {
        float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
        float(1, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
    })

}