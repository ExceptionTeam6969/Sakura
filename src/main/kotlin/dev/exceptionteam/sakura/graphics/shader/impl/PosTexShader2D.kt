package dev.exceptionteam.sakura.graphics.shader.impl

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.Shader
import org.lwjgl.opengl.GL45

object PosTexShader2D: Shader(
    "${Sakura.ASSETS_DIRECTORY}/shader/general/PosTex2D.vert",
    "${Sakura.ASSETS_DIRECTORY}/shader/general/PosTex2D.frag",
) {

    private val matrixLocation = GL45.glGetUniformLocation(id, "MVPMatrix")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
    }

}