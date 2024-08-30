package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.graphics.GlObject
import org.lwjgl.opengl.GL45.*

class Texture: GlObject {
    override var id: Int = glGenTextures()

    override fun bind() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun delete() {
        glDeleteTextures(id)
    }

    fun use(func: ()->Unit) {
        bind()
        func()
        unbind()
    }

}