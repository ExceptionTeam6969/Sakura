package dev.exceptionteam.sakura.graphics.shader.impl

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.Shader
import org.lwjgl.opengl.GL20.glGetUniformLocation

object PosColorShader2D: Shader(
    vertShaderPath = "${Sakura.ASSETS_DIRECTORY}/shader/general/PosColor2D.vert",
    fragShaderPath = "${Sakura.ASSETS_DIRECTORY}/shader/general/PosColor.frag"
) {

    private val matrixLocation = glGetUniformLocation(id, "MVPMatrix")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
    }

}