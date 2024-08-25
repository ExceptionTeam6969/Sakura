package dev.exceptionteam.sakura.graphics.shader

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.GlDataType
import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.buffer.buildAttribute
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45

object FontRendererShader: Shader(
    "${Sakura.ASSETS_DIRECTORY}/shader/font/FontRenderer.vert",
    "${Sakura.ASSETS_DIRECTORY}/shader/font/FontRenderer.frag",
) {

    private val matrixLocation = GL45.glGetUniformLocation(id, "MVPMatrix")
    private val textureLocation = GL45.glGetUniformLocation(id, "FontTexture")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
        set(textureLocation, 0)
    }

    val vao = createVao(buildAttribute(PMBuffer.STRIDE) {
        float(0, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
        float(1, 2, GlDataType.GL_FLOAT, false)         // 8 bytes
        float(2, 4, GlDataType.GL_UNSIGNED_BYTE, true)  // 4 bytes
    })

}