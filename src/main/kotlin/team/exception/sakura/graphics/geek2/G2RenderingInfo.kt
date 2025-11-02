package team.exception.sakura.graphics.geek2

abstract class G2RenderingInfo {

    abstract val colorAttachment: Attachment?

    abstract val depthAttachment: Attachment?

    abstract val width: Int

    abstract val height: Int

    data class Attachment(
        val image: G2ImageView,
        val format: G2Format,
    )

}