package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlObject
import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL45.*

class FrameBuffer : GlObject {

    override var id: Int = glCreateFramebuffers()
    private var colorAttachment = glCreateTextures(GL_TEXTURE_2D)

    init {
        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    private fun allocateFrameBuffer(width: Int, height: Int) {
        id = glCreateFramebuffers()
        colorAttachment = glCreateTextures(GL_TEXTURE_2D)

        glTextureParameteri(colorAttachment, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(colorAttachment, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(colorAttachment, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(colorAttachment, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTextureStorage2D(colorAttachment, 1, GL_RGBA8, width, height)

        glNamedFramebufferTexture(id, GL_COLOR_ATTACHMENT0, colorAttachment, 0)

        if (glCheckNamedFramebufferStatus(id, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Could not create frame buffer")
        }
    }

    fun resize() {
        glDeleteFramebuffers(id)
        glDeleteTextures(colorAttachment)

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
        glDeleteBuffers(colorAttachment)
    }

}