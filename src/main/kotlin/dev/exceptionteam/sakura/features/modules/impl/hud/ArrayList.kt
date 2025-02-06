package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.easing.Easing
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft

object ArrayList: HUDModule(
    name = "array-list",
) {
    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    private var modules: MutableList<AbstractModule> = mutableListOf()

    private val animation = hashMapOf<AbstractModule, AnimationFlag>()

    override var height: Float = 40f

    override var width: Float = 40f

    override fun render() {
        val dirUp = isDirUp()

        var deltaY: Float = if (dirUp) this.y + this.height - singleHeight else this.y
        modules.clear()


        ModuleManager.modules
            .filter { it.isDrawn }
            .sortedByDescending { module ->
                val hudInfo = module.hudInfo() ?: run {
                    return@sortedByDescending FontRenderers.getStringWidth(module.name, scale)
                }
                return@sortedByDescending FontRenderers.getStringWidth(
                    "${module.name.translation}${ChatFormatting.GRAY}[$hudInfo]", scale)
            }
            .forEach  { module ->
                val anim = animation
                    .getOrPut(module) { AnimationFlag(Easing.LINEAR, 200f) }
                        .getAndUpdate(if (module.isEnabled) 1.0f else 0.0f)

                if (module.isDisabled && anim < 0.01f) {
                    return@forEach
                }

                val hudInfo = module.hudInfo()
                val str = if (hudInfo == null) module.name.translation
                          else "${module.name.translation}${ChatFormatting.GRAY}[$hudInfo]"

                val moduleLength = FontRenderers.getStringWidth(str, scale)

                val x = (this.x + (this.width - moduleLength * anim))

                RenderUtils2D.drawRectFilled(x - 4f, deltaY, moduleLength + 6f, singleHeight, ColorRGB(0, 0, 0, 100))
                FontRenderers.drawString(str, x - 2f, deltaY, textColor, shadow, scale)

                if (dirUp) deltaY -= singleHeight * anim else deltaY += singleHeight * anim
                modules.add(module)
            }

    }
    
    private val singleHeight get() = FontRenderers.getHeight(scale)

    /**
     * Get the direction of the notification based on the y position of the notification.
     * @return true up, false down
     */
    private fun isDirUp(): Boolean =
        if (y > Minecraft.getInstance().window.guiScaledHeight / 2) true else false
}