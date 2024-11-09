package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.texture.ImageFileUtils
import dev.exceptionteam.sakura.graphics.texture.Texture
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.resources.Resource
import dev.exceptionteam.sakura.utils.threads.GameThreadUtils
import net.minecraft.client.MinecraftClient

object GuiImage: HUDModule(
    name = "gui-image",
    width = 40f,
    height = 20f,
) {
    private val image by setting("image", Image.QIANSHU)
    private val scale by setting("scale", 1f, 0.1f..10f)

    var qianshuTex: Texture? = null
    var mahiroTex: Texture? = null

    private val mc get() = MinecraftClient.getInstance()

    init {
        GameThreadUtils.runOnRenderThread {
            qianshuTex = ImageFileUtils.loadTextureFromResource(Resource("gui/qianshu.png"))
            mahiroTex = ImageFileUtils.loadTextureFromResource(Resource("gui/mahiro.png"))
        }

        listener<Render2DEvent>(priority = -200) {
            if (mc.currentScreen != ClickGUIScreen && mc.currentScreen != HUDEditorScreen) return@listener

            when (image) {
                Image.QIANSHU -> qianshuTex?.draw()
                Image.ZHENXUN -> mahiroTex?.draw()
            }
        }
    }

    override fun render() {
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