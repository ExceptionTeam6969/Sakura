package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlObject
import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL45.*

class FrameBuffer : GlObject {

    override var id: Int = glGenFramebuffers()
    private var textureId = glGenTextures()

    init {
        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    private fun allocateFrameBuffer(width: Int, height: Int) {
        id = glGenFramebuffers()
        textureId = glGenTextures()

        glBindFramebuffer(GL_FRAMEBUFFER, id)
        glBindTexture(GL_TEXTURE_2D, textureId)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height,
            0, GL_RGBA, GL_UNSIGNED_BYTE, 0)

        glBindTexture(GL_TEXTURE_2D, 0)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun resize() {
        glDeleteFramebuffers(id)
        glDeleteTextures(textureId)

        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun delete() {
        glDeleteFramebuffers(id)
        glDeleteBuffers(textureId)
    }

}