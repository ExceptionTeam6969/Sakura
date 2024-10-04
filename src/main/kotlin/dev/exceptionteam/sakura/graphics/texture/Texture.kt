package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.graphics.GlObject
import org.lwjgl.opengl.GL45.*

class Texture(type: Int = GL_TEXTURE_2D): GlObject {

    override var id: Int = glCreateTextures(type)

    override fun bind() {
        glBindTextureUnit(0, id)
    }

    override fun unbind() {
        glBindTextureUnit(0, 0)
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