package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.WindowResizeEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.utils.math.vector.Vec3f
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Box
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL45.*

object RenderUtils3D {

    var lastMvpMatrix: Matrix4f = Matrix4f(); private set
    var lastPosMatrix: Matrix4f = Matrix4f(); private set

    private val viewport = IntArray(4)

    init {

        glGetIntegerv(GL_VIEWPORT, viewport)

        listener<Render3DEvent>(alwaysListening = true, priority = Int.MIN_VALUE) {
            lastMvpMatrix = Matrix4f(MatrixStack.peek().mvpMatrix)
            lastPosMatrix = Matrix4f(MatrixStack.peek().positionMatrix)
        }

        listener<WindowResizeEvent>(alwaysListening = true) {
            glGetIntegerv(GL_VIEWPORT, viewport)
        }

    }

    /**
     * Convert a vector in world space to screen space.
     */
    fun NonNullContext.worldSpaceToScreenSpace(pos: Vec3f): Vec3f {
        val camera = mc.entityRenderDispatcher.camera
        val displayHeight = mc.window.height

        val target = Vector3f()

        val deltaX = pos.x - camera.pos.x
        val deltaY = pos.y - camera.pos.y
        val deltaZ = pos.z - camera.pos.z

        val transformedCoordinates =
            Vector4f(deltaX.toFloat(), deltaY.toFloat(), deltaZ.toFloat(), 1f).mul(lastPosMatrix)

        lastMvpMatrix.project(
            transformedCoordinates.x(),
            transformedCoordinates.y(),
            transformedCoordinates.z(),
            viewport,
            target
        )

        return Vec3f(
            target.x / mc.window.scaleFactor.toFloat(),
            (displayHeight - target.y) / mc.window.scaleFactor.toFloat(), target.z
        )
    }

    fun drawFilledBox(box: Box, color: ColorRGB) {
        val mc = MinecraftClient.getInstance()
        val camera = mc.gameRenderer.camera

        val minX = (box.minX - camera.pos.getX()).toFloat()
        val minY = (box.minY - camera.pos.getY()).toFloat()
        val minZ = (box.minZ - camera.pos.getZ()).toFloat()
        val maxX = (box.maxX - camera.pos.getX()).toFloat()
        val maxY = (box.maxY - camera.pos.getY()).toFloat()
        val maxZ = (box.maxZ - camera.pos.getZ()).toFloat()

        GL_TRIANGLES.draw(VertexBufferObjects.PosColor3D) {
            vertex(minX, minY, minZ, color)
            vertex(maxX, minY, minZ, color)
            vertex(maxX, maxY, minZ, color)
            vertex(minX, minY, minZ, color)
            vertex(maxX, maxY, minZ, color)
            vertex(minX, maxY, minZ, color)

            vertex(minX, minY, minZ, color)
            vertex(maxX, minY, minZ, color)
            vertex(maxX, minY, maxZ, color)
            vertex(minX, minY, minZ, color)
            vertex(maxX, minY, maxZ, color)
            vertex(minX, minY, maxZ, color)

            vertex(maxX, minY, minZ, color)
            vertex(maxX, maxY, minZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(maxX, minY, minZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(maxX, minY, maxZ, color)

            vertex(minX, minY, maxZ, color)
            vertex(maxX, minY, maxZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(minX, minY, maxZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(minX, maxY, maxZ, color)

            vertex(minX, minY, minZ, color)
            vertex(minX, maxY, minZ, color)
            vertex(minX, maxY, maxZ, color)
            vertex(minX, minY, minZ, color)
            vertex(minX, maxY, maxZ, color)
            vertex(minX, minY, maxZ, color)

            vertex(minX, maxY, minZ, color)
            vertex(maxX, maxY, minZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(minX, maxY, minZ, color)
            vertex(maxX, maxY, maxZ, color)
            vertex(minX, maxY, maxZ, color)
        }
    }

}