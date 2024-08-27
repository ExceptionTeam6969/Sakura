package dev.exceptionteam.sakura.graphics.texture

import org.lwjgl.opengl.GL45

object TextureUtils {

    fun useTexture(texture: Int) {
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, texture)
    }

    fun useTexture(texture: Int, func: ()->Unit) {
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, texture)
        func.invoke()
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0)
    }

}