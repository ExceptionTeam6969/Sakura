package team.exception.sakura.graphics.geek2

abstract class G2CommandList {

    /**
     * Binds the graphics pipeline to the command list.
     */
    abstract fun bindGraphicsPipeline(pipeline: G2GraphicsPipeline)

    /**
     * Draw array
     */
    abstract fun draw(vertexCount: Int, instanceCount: Int, firstVertex: Int, firstInstance: Int)

    /**
     * Draw indexed
     */
    abstract fun drawIndexed(indexCount: Int, instanceCount: Int, firstIndex: Int, vertexOffset: Int, firstInstance: Int)

    /**
     * Begin rendering (Dynamic rendering in vulkan)
     */
    abstract fun beginRendering(renderingInfo: G2RenderingInfo)

    /**
     * End rendering (Dynamic rendering in vulkan)
     */
    abstract fun endRendering()

    /**
     * Summit the command list
     */
    abstract fun summit()

    /**
     * Clear the command list
     */
    abstract fun clear()

    /**
     * Summit and clear the command list
     */
    abstract fun summitAndClear()

    /**
     * Destroy the command list
     */
    abstract fun destroy()

    /**
     * Summit and destroy the command list
     */
    abstract fun summitAndDestroy()

}