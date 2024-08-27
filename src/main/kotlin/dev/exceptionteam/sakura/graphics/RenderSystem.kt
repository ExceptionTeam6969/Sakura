package dev.exceptionteam.sakura.graphics

import com.mojang.blaze3d.systems.RenderSystem
import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.WindowResizeEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.modules.impl.client.RenderSystemMod
import dev.exceptionteam.sakura.graphics.buffer.FrameBuffer
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Matrix4f
import org.lwjgl.opengl.GL45

object RenderSystem {

    // OpenGL version
    private val glVersion = GL45.glGetString(GL45.GL_VERSION) ?: ""
    private val gpuManufacturer = GL45.glGetString(GL45.GL_VENDOR) ?: ""
    private val gpuName = GL45.glGetString(GL45.GL_RENDERER)?.substringBefore("/") ?: ""

    private val intelGraphics = glVersion.lowercase().contains("intel")
            || gpuManufacturer.lowercase().contains("intel")
            || gpuName.lowercase().contains("intel")

    private val amdGraphics = glVersion.lowercase().contains("amd")
            || gpuManufacturer.lowercase().contains("amd")
            || gpuName.lowercase().contains("amd")

    private val nvidiaGraphics = glVersion.lowercase().contains("nvidia")
            || gpuManufacturer.lowercase().contains("nvidia")
            || gpuName.lowercase().contains("nvidia")

    val gpuType: GPUType get() {
        if (intelGraphics) return GPUType.INTEL
        if (amdGraphics) return GPUType.AMD
        if (nvidiaGraphics) return GPUType.NVIDIA
        return GPUType.OTHER
    }

    enum class GPUType {
        INTEL,
        AMD,
        NVIDIA,
        OTHER
    }

    private val frameBuffer = FrameBuffer()

    init {

        listener<WindowResizeEvent>(alwaysListening = true) {
            frameBuffer.resize()
        }

    }

    fun onRender2d(context: DrawContext) {
        preRender2d()

        val modelView = Matrix4f(RenderSystem.getModelViewMatrix())
        val projection = Matrix4f(RenderSystem.getProjectionMatrix())
        MatrixStack.peek().mvpMatrix.set(projection.mul(modelView))
        Render2DEvent(context).post()

        postRender2d()
    }

    private fun preRender2d() {
        VertexBufferObjects.sync()
        GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA)
        GL45.glEnable(GL45.GL_BLEND)
        preFrameBuffer()
    }

    private fun postRender2d() {
        GL45.glDisable(GL45.GL_BLEND)
        postFrameBuffer()
    }

    fun onRender3d() {
        preRender3d()

        Render3DEvent().post()

        postRender3d()
    }

    private fun preRender3d() {
        VertexBufferObjects.sync()
        preFrameBuffer()
        GL45.glDisable(GL45.GL_DEPTH_TEST)
    }

    private fun postRender3d() {
        postFrameBuffer()
        GL45.glEnable(GL45.GL_DEPTH_TEST)
    }

    private fun preFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return
        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, frameBuffer.id)
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, mc.framebuffer.fbo)
        GL45.glBlitFramebuffer(0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL45.GL_COLOR_BUFFER_BIT, GL45.GL_NEAREST)
    }

    private fun postFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return

        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, mc.framebuffer.fbo)
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, frameBuffer.id)
        GL45.glBlitFramebuffer(0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL45.GL_COLOR_BUFFER_BIT, GL45.GL_NEAREST)
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, mc.framebuffer.fbo)
    }

}