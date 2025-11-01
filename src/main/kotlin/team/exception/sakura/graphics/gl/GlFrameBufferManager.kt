package team.exception.sakura.graphics.gl

import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.opengl.GlDevice as MojGlDevice
import net.minecraft.client.Minecraft
import team.exception.sakura.graphics.G2RenderSystem
import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.geek2.G2RenderingInfo


object GlFrameBufferManager {

    val mcFboId = (Minecraft.getInstance().mainRenderTarget.colorTexture as GlTexture)
        .getFbo((RenderSystem.getDevice() as MojGlDevice).directStateAccess(),
            Minecraft.getInstance().mainRenderTarget.depthTexture)

    // FrameBuffer -> OpenGl FBO ID
    private val frameBuffers = mutableMapOf<FrameBuffer, Int>()

    /**
     * Get a FrameBuffer by attachments.
     * If attachments are null, return the Minecraft FrameBuffer.
     * @return OpenGl FBO ID
     */
    fun getFrameBufferByAttachments(
        colorAttachment: GlImageView?,
        depthAttachment: GlImageView?,
    ): Int {
        if (colorAttachment == null || depthAttachment == null) {
            return mcFboId
        }
        val frameBuffer = FrameBuffer(colorAttachment, depthAttachment)
        return frameBuffers.getOrPut(frameBuffer) {
            val fboId = createFrameBuffer(colorAttachment, depthAttachment)
            frameBuffers[frameBuffer] = fboId
            fboId
        }
    }

    fun getFrameBufferByAttachments(
        colorAttachment: G2RenderingInfo.Attachment?,
        depthAttachment: G2RenderingInfo.Attachment?,
    ): Int = getFrameBufferByAttachments(
        colorAttachment?.image as GlImageView?,
        depthAttachment?.image as GlImageView?,
    )

    /**
     * Create a FrameBuffer with attachments.
     * @param colorAttachment the color attachment of the FrameBuffer, null if not needed
     * @param depthAttachment the depth attachment of the FrameBuffer, null if not needed
     * @throws IllegalStateException if FrameBuffer creation failed
     * @return OpenGl FBO ID
     */
    private fun createFrameBuffer(
        colorAttachment: GlImageView?,
        depthAttachment: GlImageView?,
    ): Int {

        val device = G2RenderSystem.device as GlDevice

        val fboId = glGenFramebuffers()
        val cmdList = device.getTempCommandList()

        cmdList.add {
            val prevFboId = glGetInteger(GL_FRAMEBUFFER_BINDING)
            GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, fboId)
            colorAttachment?.let {
                glBindTexture(GL_TEXTURE_2D, it.image.glId)
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                    GL_TEXTURE_2D, it.image.glId, 0)
            }
            depthAttachment?.let {
                glBindTexture(GL_TEXTURE_2D, it.image.glId)
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                    GL_TEXTURE_2D, it.image.glId, 0)
            }
            val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
            if (status != GL_FRAMEBUFFER_COMPLETE) {
                throw IllegalStateException("FrameBuffer creation failed: $status")
            }
            GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, prevFboId)
        }
        cmdList.summitAndClear()

        return fboId

    }

    class FrameBuffer(
        val colorImage: GlImageView? = null,
        val depthImage: GlImageView? = null,
    ) {
        override fun hashCode(): Int {
            val colorHash = colorImage?.image?.glId ?: 0
            val depthHash = depthImage?.image?.glId ?: 0
            return colorHash + depthHash
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FrameBuffer

            if (colorImage?.image?.glId != other.colorImage?.image?.glId) return false
            if (depthImage?.image?.glId != other.depthImage?.image?.glId) return false

            return true
        }
    }

}
