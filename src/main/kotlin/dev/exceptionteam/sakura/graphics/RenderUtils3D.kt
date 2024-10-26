package dev.exceptionteam.sakura.graphics

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects.draw
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.matrix.MatrixStack
import dev.exceptionteam.sakura.utils.math.vector.Vec3f
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Box
import org.joml.Vector4f
import org.lwjgl.opengl.GL45.*

object RenderUtils3D {

    /**
     * Convert a vector in world space to screen space.
     */
    fun NonNullContext.worldSpaceToScreenSpace(pos: Vec3f): Vec3f {
        val camera = mc.gameRenderer.camera

        val x0 = (camera.pos.x - pos.x).toFloat()
        val y0 = (camera.pos.y - pos.y).toFloat()
        val z0 = (camera.pos.z - pos.z).toFloat()

        val position0 = MatrixStack.getPosition(x0, y0, z0)

        val position =
            MatrixStack.peek().mvpMatrix.transform(Vector4f(position0.x, position0.y, position0.z, 1.0f))

        return Vec3f(position.x, position.y, position.z)
    }

    fun drawFilledBox(box: Box, color: ColorRGB) {
        val mc = MinecraftClient.getInstance()

        val minX = (box.minX - mc.entityRenderDispatcher.camera.pos.getX()).toFloat()
        val minY = (box.minY - mc.entityRenderDispatcher.camera.pos.getY()).toFloat()
        val minZ = (box.minZ - mc.entityRenderDispatcher.camera.pos.getZ()).toFloat()
        val maxX = (box.maxX - mc.entityRenderDispatcher.camera.pos.getX()).toFloat()
        val maxY = (box.maxY - mc.entityRenderDispatcher.camera.pos.getY()).toFloat()
        val maxZ = (box.maxZ - mc.entityRenderDispatcher.camera.pos.getZ()).toFloat()

        GL_TRIANGLES.draw(VertexBufferObjects.PosColor3D) {
            // TODO: Add vertexes to buffer
        }
    }

}