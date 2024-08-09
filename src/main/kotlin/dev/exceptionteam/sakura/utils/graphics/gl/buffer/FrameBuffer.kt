package dev.exceptionteam.sakura.utils.graphics.gl.buffer

import dev.exceptionteam.sakura.utils.graphics.gl.GlObject
import org.lwjgl.opengl.GL45.*
import java.nio.ByteBuffer

open class FrameBuffer : GlObject {

    override val id: Int = glGenFramebuffers()
    private val textureId = glGenTextures()

    open fun allocateFrameBuffer(width: Int, height: Int) {
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null as ByteBuffer?)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glBindTexture(GL_TEXTURE_2D, 0)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)
    }

    fun bindTexture() {
        glBindTexture(GL_TEXTURE_2D, textureId)
    }

    fun unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun destroy() {
        glDeleteFramebuffers(id)
    }


}