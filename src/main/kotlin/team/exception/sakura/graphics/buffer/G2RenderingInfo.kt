package team.exception.sakura.graphics.buffer

abstract class G2RenderingInfo {

    abstract val colorAttachment: Attachment?

    abstract val depthAttachment: Attachment?

    abstract val stencilAttachment: Attachment?

    abstract val width: Int

    abstract val height: Int

    data class Attachment(
        val image: G2Image,
        val format: G2Format,
    )

}