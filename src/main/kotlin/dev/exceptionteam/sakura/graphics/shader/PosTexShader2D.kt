package dev.exceptionteam.sakura.graphics.shader

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import org.lwjgl.opengl.GL45

object PosTexShader2D: Shader(
    "${Sakura.ASSETS_DIRECTORY}/shader/font/FontRenderer.vert",
    "${Sakura.ASSETS_DIRECTORY}/shader/font/FontRenderer.frag",
) {

    private val matrixLocation = GL45.glGetUniformLocation(id, "MVPMatrix")
//    private val textureLocation = GL45.glGetUniformLocation(id, "FontTexture")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
//        set(textureLocation, 0)
    }

}