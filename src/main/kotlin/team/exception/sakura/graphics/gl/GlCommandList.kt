package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.geek2.G2CommandList
import team.exception.sakura.graphics.geek2.G2GraphicsPipeline
import team.exception.sakura.graphics.geek2.G2RenderingInfo
import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.gl.GlGraphicsPipeline.Companion.toGlPrimitive
import java.util.Stack

class GlCommandList: G2CommandList() {

    private val commands: MutableList<GlCommand> = mutableListOf()
    private var graphicsPipeline: GlGraphicsPipeline? = null
    private val renderingInfoStack: Stack<G2RenderingInfo> = Stack()

    internal fun add(command: () -> Unit) {
        commands.add(GlCommand(command))
    }

    override fun bindGraphicsPipeline(pipeline: G2GraphicsPipeline) {
        graphicsPipeline = pipeline as GlGraphicsPipeline
    }

    override fun draw(
        vertexCount: Int,
        instanceCount: Int,
        firstVertex: Int,
        firstInstance: Int
    ) {
        graphicsPipeline?.let { pipeline -> add {
            glDrawArrays(
                pipeline.primitive.toGlPrimitive(),
                firstVertex, vertexCount
            )
        } }
    }

    override fun drawIndexed(
        indexCount: Int,
        instanceCount: Int,
        firstIndex: Int,
        vertexOffset: Int,
        firstInstance: Int
    ) {
        graphicsPipeline?.let { pipeline -> add {
            glDrawElements(
                pipeline.primitive.toGlPrimitive(),
                indexCount, GL_UNSIGNED_INT, (firstIndex * 4).toLong()
            )
        } }
    }

    override fun beginRendering(renderingInfo: G2RenderingInfo) {
        renderingInfoStack.push(renderingInfo)
    }

    override fun endRendering() {
        renderingInfoStack.pop()
    }

    override fun summit() {
        commands.forEach { it.func() }
    }

    override fun clear() {
        commands.clear()
    }

    override fun summitAndClear() {
        summit()
        clear()
    }

    class GlCommand(val func: () -> Unit)

}