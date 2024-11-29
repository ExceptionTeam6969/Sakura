package dev.exceptionteam.sakura.graphics.buffer

import dev.exceptionteam.sakura.graphics.GlObject
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL45.*

class FrameBuffer : GlObject {

    override var id: Int = glCreateFramebuffers()
    var colorAtt = glCreateTextures(GL_TEXTURE_2D) ;private set
    var depthStencilAtt = glCreateRenderbuffers() ;private set

    init {
        val mc = Minecraft.getInstance()
        allocateFrameBuffer(mc.window.width, mc.window.height)
    }

    private fun allocateFrameBuffer(width: Int, height: Int) {
        id = glCreateFramebuffers()
        colorAtt = glCreateTextures(GL_TEXTURE_2D)
        depthStencilAtt = glCreateRenderbuffers()

        /* Color Attachment */
        glTextureStorage2D(colorAtt, 1, GL_RGBA8, width, height)
        glTextureParameteri(colorAtt, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(colorAtt, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTextureParameteri(colorAtt, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(colorAtt, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glNamedFramebufferTexture(id, GL_COLOR_ATTACHMENT0, colorAtt, 0)

        /* Depth Attachment & Stencil Attachment */
        glNamedRenderbufferStorage(depthStencilAtt, GL_DEPTH24_STENCIL8, width, height)
        glNamedFramebufferRenderbuffer(id, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthStencilAtt)

        if (glCheckNamedFramebufferStatus(id, GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Could not create frame buffer")
        }
    }

    fun resize() {
        glDeleteFramebuffers(id)
        glDeleteTextures(colorAtt)
        glDeleteRenderbuffers(depthStencilAtt)

        val mc = Minecraft.getInstance()
        allocateFrameBuffer(mc.window.width, mc.window.height)
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun delete() {
        glDeleteFramebuffers(id)
        glDeleteTextures(colorAtt)
        glDeleteRenderbuffers(depthStencilAtt)
    }

}