package dev.exceptionteam.sakura.graphics

import com.mojang.blaze3d.systems.RenderSystem
import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.WindowResizeEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.modules.impl.client.RenderSystemMod
import dev.exceptionteam.sakura.graphics.buffer.FrameBuffer
import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.graphics.shader.PosColorShader2D
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Matrix4f
import org.lwjgl.opengl.GL45

object RenderSystem {

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
        PMBuffer.onSync()
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
        PMBuffer.onSync()
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