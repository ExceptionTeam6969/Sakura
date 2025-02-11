package dev.exceptionteam.sakura.graphics.texture

import org.lwjgl.opengl.ARBBindlessTexture.*
import org.lwjgl.opengl.GL45.*

class BindLessTexture(type: Int = GL_TEXTURE_2D): Texture(type) {
    // Gl Unsigned Int 64
    var handle: Long = 0; private set

    fun generateHandle() {
        handle = glGetTextureHandleARB(id)
    }

    override fun bind() {
        glMakeTextureHandleResidentARB(handle)
    }

    override fun use(func: () -> Unit) {
        bind()
        func()
        unbind()
    }

    override fun unbind() {
        glMakeTextureHandleNonResidentARB(handle)
    }

    fun checkHandle() = glIsTextureHandleResidentARB(handle)
}