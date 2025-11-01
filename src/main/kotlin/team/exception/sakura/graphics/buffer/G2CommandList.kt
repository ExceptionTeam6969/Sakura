package team.exception.sakura.graphics.buffer

abstract class G2CommandList {

    abstract fun bindGraphicsPipeline(pipeline: G2GraphicsPipeline)

    abstract fun draw(vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int)

    abstract fun drawIndexed(indexCount: Int, instanceCount: Int, firstIndex: Int, vertexOffset: Int, firstInstance: Int)

    abstract fun beginRendering(renderingInfo: G2RenderingInfo)

    abstract fun endRendering()

    abstract fun summit()

    abstract fun clear()

    abstract fun summitAndClear()

}