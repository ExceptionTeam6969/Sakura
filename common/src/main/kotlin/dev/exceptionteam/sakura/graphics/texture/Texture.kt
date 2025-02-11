package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.graphics.GlHelper
import dev.exceptionteam.sakura.graphics.GlObject
import org.lwjgl.opengl.GL45.*

open class Texture(type: Int = GL_TEXTURE_2D): GlObject {

    init {
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0)
        glPixelStorei(GL_UNPACK_SKIP_ROWS, 0)
        glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0)
    }

    override var id: Int = glCreateTextures(type)

    var width: Int = 0
    var height: Int = 0

    override fun bind() {
        GlHelper.bindTexture(0, id)
    }

    override fun unbind() {
        GlHelper.bindTexture(0, 0)
    }

    override fun delete() {
        glDeleteTextures(id)
    }

    open fun use(func: ()->Unit) {
        bind()
        func()
    }

}