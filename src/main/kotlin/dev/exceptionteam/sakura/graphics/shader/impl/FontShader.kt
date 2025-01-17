package dev.exceptionteam.sakura.graphics.shader.impl

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.Shader
import org.lwjgl.opengl.ARBBindlessTexture.glProgramUniformHandleui64ARB
import org.lwjgl.opengl.GL45

// Only for sparse font mode
object FontShader: Shader(
    "${Sakura.ASSETS_DIRECTORY}/shader/general/FontRenderer.vert",
    "${Sakura.ASSETS_DIRECTORY}/shader/general/FontRenderer.frag",
) {

    private val matrixLocation = GL45.glGetUniformLocation(id, "MVPMatrix")
    private val samplerLocation = GL45.glGetUniformLocation(id, "u_Texture")

    override fun default() {
        set(matrixLocation, MatrixStack.peek().mvpMatrix)
        textureUnit?.let { glProgramUniformHandleui64ARB(id, samplerLocation, it) }
    }

    var textureUnit: Long? = 0L
}