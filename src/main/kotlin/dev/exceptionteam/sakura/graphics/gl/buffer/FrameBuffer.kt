package dev.exceptionteam.sakura.graphics.gl.buffer

import com.mojang.blaze3d.platform.GlStateManager
import dev.exceptionteam.sakura.graphics.gl.GlObject
import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL45


class FrameBuffer : GlObject {

    override var id: Int = GL45.glGenFramebuffers()
    private var textureId = GL45.glGenTextures()

    init {
        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    private fun allocateFrameBuffer(width: Int, height: Int) {
        id = GL45.glGenFramebuffers()
        textureId = GL45.glGenTextures()

        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, id)
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, textureId)

        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_S, GL45.GL_CLAMP_TO_EDGE)
        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_T, GL45.GL_CLAMP_TO_EDGE)
        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_LINEAR)
        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_LINEAR)

        GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA8, width, height,
            0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, 0)

        GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0)

        GL45.glFramebufferTexture2D(GL45.GL_FRAMEBUFFER, GL45.GL_COLOR_ATTACHMENT0, GL45.GL_TEXTURE_2D, textureId, 0)
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, 0)
    }

    fun resize() {
        GL45.glDeleteFramebuffers(id)
        GL45.glDeleteTextures(textureId)

        val mc = MinecraftClient.getInstance()
        allocateFrameBuffer(mc.window.framebufferWidth, mc.window.framebufferHeight)
    }

    override fun bind() {
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, id)
    }

    override fun unbind() {
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, 0)
    }

    override fun delete() {
        GL45.glDeleteFramebuffers(id)
        GL45.glDeleteBuffers(textureId)
    }

}