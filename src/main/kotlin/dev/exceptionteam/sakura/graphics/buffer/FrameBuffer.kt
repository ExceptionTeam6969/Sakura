package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlObject
import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL45.*

class FrameBuffer : GlObject {

    override var id: Int = glCreateFramebuffers()
    var colorAtt = glCreateRenderbuffers() ;private set
    var depthStencilAtt = glCreateRenderbuffers() ;private set

    init {
        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    private fun allocateFrameBuffer(width: Int, height: Int) {
        id = glCreateFramebuffers()
        colorAtt = glCreateRenderbuffers()
        depthStencilAtt = glCreateRenderbuffers()

        /* Color Attachment */
        glNamedRenderbufferStorage(colorAtt, GL_RGBA8, width, height)
        glNamedFramebufferRenderbuffer(id, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorAtt)

        /* Depth Attachment & Stencil Attachment */
        glNamedRenderbufferStorage(depthStencilAtt, GL_DEPTH24_STENCIL8, width, height)
        glNamedFramebufferRenderbuffer(id, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthStencilAtt)

        if (glCheckNamedFramebufferStatus(id, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Could not create frame buffer")
        }
    }

    fun resize() {
        glDeleteFramebuffers(id)
        glDeleteRenderbuffers(colorAtt)
        glDeleteRenderbuffers(depthStencilAtt)

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
        glDeleteRenderbuffers(colorAtt)
        glDeleteRenderbuffers(depthStencilAtt)
    }

}