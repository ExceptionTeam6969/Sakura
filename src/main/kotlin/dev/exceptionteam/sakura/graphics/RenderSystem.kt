package dev.exceptionteam.sakura.graphics

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.math.Axis
import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.WindowResizeEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen
import dev.exceptionteam.sakura.features.modules.impl.client.RenderSystemMod
import dev.exceptionteam.sakura.graphics.buffer.FrameBuffer
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import net.minecraft.client.Minecraft
import org.joml.Matrix4f
import org.lwjgl.opengl.GL45.*

object RenderSystem {

    private val mc
        get() = Minecraft.getInstance()

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

    fun onRender2d() {
        if (mc.screen != null && mc.screen != ClickGUIScreen && mc.screen != HUDEditorScreen) return

        preRender()

        MatrixStack.scope {
            val projection = Matrix4f(RenderSystem.getProjectionMatrix())
            val modelView = Matrix4f(RenderSystem.getModelViewMatrix())
            updateMvpMatrix(projection.mul(modelView))
            Render2DEvent().post()
        }

        postRender()
    }

    fun onRender3d() {
        preRender()

        GlHelper.depth = false
        GlHelper.cull = false

        MatrixStack.scope {
            val camera = mc.gameRenderer.mainCamera

            val projection = Matrix4f(RenderSystem.getProjectionMatrix())
            val modelView = Matrix4f(RenderSystem.getModelViewMatrix())

            multiply(Axis.XP.rotationDegrees(camera.xRot))
            multiply(Axis.YP.rotationDegrees(camera.yRot + 180.0f))

            updateMvpMatrix(projection.mul(modelView))
            Render3DEvent().post()
        }

        postRender()
    }

    private fun preRender() {
        preAttrib()
        GlHelper.syncWithMinecraft()
        GlHelper.reset()
        VertexBufferObjects.sync()
        GlHelper.blend = true
        preFrameBuffer()
    }

    private fun postRender() {
        GlHelper.blend = true
        GlHelper.depth = true
        GlHelper.cull = true
        postFrameBuffer()
        postAttrib()
        GlHelper.syncWithMinecraft()
    }

    private fun preFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return

        val wWidth = mc.window.width
        val wHeight = mc.window.height

        glBlitNamedFramebuffer(mc.mainRenderTarget.frameBufferId, frameBuffer.id,
            0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL_COLOR_BUFFER_BIT, GL_NEAREST)
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer.id)
    }

    private fun postFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return

        val wWidth = mc.window.width
        val wHeight = mc.window.height

        glBlitNamedFramebuffer(frameBuffer.id, mc.mainRenderTarget.frameBufferId,
            0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL_COLOR_BUFFER_BIT, GL_NEAREST)
        glBindFramebuffer(GL_FRAMEBUFFER, mc.mainRenderTarget.frameBufferId)
    }

    /* Attrib */
    private var vaoLast = -1
    private var vboLast = -1
    private var eboLast = -1
    private var lastShader = -1

    private fun preAttrib() {
        vaoLast = glGetInteger(GL_VERTEX_ARRAY_BINDING)
        vboLast = glGetInteger(GL_ARRAY_BUFFER_BINDING)
        eboLast = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING)
        lastShader = glGetInteger(GL_CURRENT_PROGRAM)
    }

    private fun postAttrib() {
        glBindVertexArray(vaoLast)
        glBindBuffer(GL_ARRAY_BUFFER, vboLast)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboLast)
        glUseProgram(lastShader)
    }

}