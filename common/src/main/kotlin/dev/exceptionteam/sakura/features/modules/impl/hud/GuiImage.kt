package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.graphics.texture.ImageFileUtils
import dev.exceptionteam.sakura.graphics.texture.Texture
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.resources.Resource
import dev.exceptionteam.sakura.utils.threads.GameThreadUtils
import net.minecraft.client.Minecraft

object GuiImage: HUDModule(
    name = "gui-image",
) {
    private val image by setting("image", Image.QIANSHU)
    private val scale by setting("scale", 1f, 0.1f..10f)

    private var img: Texture? = null

    var qianshuTex: Texture? = null
    var mahiroTex: Texture? = null

    override var width: Float = 50f
        get() = (img?.width?.times(scale)?.times(0.2f)) ?: 50f
    override var height: Float = 50f
        get() = (img?.height?.times(scale)?.times(0.2f)) ?: 50f

    private val mc get() = Minecraft.getInstance()

    init {
        GameThreadUtils.runOnRenderThread {
            qianshuTex = ImageFileUtils.loadTextureFromResource(Resource("gui/qianshu.png"))
            mahiroTex = ImageFileUtils.loadTextureFromResource(Resource("gui/mahiro.png"))
        }
    }

    override fun render() {
    }

    fun renderImage() {
        if (isDisabled) return
        if (mc.screen != ClickGUIScreen && mc.screen != HUDEditorScreen) return

        img = when (image) {
            Image.QIANSHU -> qianshuTex
            Image.ZHENXUN -> mahiroTex
    //            else -> img = null
        }

        img?.draw()
    }

    private fun Texture.draw() {
        val width = this.width * scale * 0.2f
        val height = this.height * scale * 0.2f
        RenderUtils2D.drawTextureRect(x, y, width, height, this)
    }

    private enum class Image(override val key: CharSequence): TranslationEnum {
        QIANSHU("qianshu"),
        ZHENXUN("zhenxun")
    }
}