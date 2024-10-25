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
import org.lwjgl.opengl.GL45.*

object RenderSystem {

    // OpenGL version
    private val glVersion = glGetString(GL_VERSION) ?: ""
    private val gpuManufacturer = glGetString(GL_VENDOR) ?: ""
    private val gpuName = glGetString(GL_RENDERER)?.substringBefore("/") ?: ""

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
        preAttrib()
        VertexBufferObjects.sync()
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        preFrameBuffer()
    }

    private fun postRender2d() {
        glDisable(GL_BLEND)
        postFrameBuffer()
        postAttrib()
    }

    fun onRender3d() {
        preRender3d()

        Render3DEvent().post()

        postRender3d()
    }

    private fun preRender3d() {
        preAttrib()
        VertexBufferObjects.sync()
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        preFrameBuffer()
        glDisable(GL_DEPTH_TEST)
    }

    private fun postRender3d() {
        glDisable(GL_BLEND)
        postFrameBuffer()
        glEnable(GL_DEPTH_TEST)
        postAttrib()
    }

    private fun preFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return
        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer.id)
        glBlitNamedFramebuffer(mc.framebuffer.fbo, frameBuffer.id,
            0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL_COLOR_BUFFER_BIT, GL_NEAREST)
    }

    private fun postFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return

        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        glBindFramebuffer(GL_FRAMEBUFFER, mc.framebuffer.fbo)
        glBlitNamedFramebuffer(frameBuffer.id, mc.framebuffer.fbo,
            0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL_COLOR_BUFFER_BIT, GL_NEAREST)
    }

    // Attrib
    private var vaoLast = -1
    private var vboLast = -1
    private var eboLast = -1
    private var lastShader = -1
    private var lastTexture = -1

    private fun preAttrib() {
        vaoLast = glGetInteger(GL_VERTEX_ARRAY_BINDING)
        vboLast = glGetInteger(GL_ARRAY_BUFFER_BINDING)
        eboLast = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING)
        lastShader = glGetInteger(GL_CURRENT_PROGRAM)
        lastTexture = glGetInteger(GL_TEXTURE_BINDING_2D)
    }

    private fun postAttrib() {
        glBindVertexArray(vaoLast)
        glBindBuffer(GL_ARRAY_BUFFER, vboLast)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboLast)
        glUseProgram(lastShader)
        glBindTexture(GL_TEXTURE_2D, lastTexture)
    }

}