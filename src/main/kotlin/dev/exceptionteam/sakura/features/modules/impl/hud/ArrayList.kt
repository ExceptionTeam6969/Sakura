package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.managers.impl.ModuleManager
import dev.exceptionteam.sakura.utils.math.MathUtils

object ArrayList: HUDModule(
    name = "array-list",
    height = 12f,
    width = 40f
) {
    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)
    private val speed by setting("animation-speed", 1.0f, 0.5f..4.0f)

    private var modules: MutableList<AbstractModule> = ArrayList()

    override var height: Float = 12f
        get() = (12f * scale * if (modules.size == 0) 1 else modules.size)

    override var width: Float = 40f
        get() = FontRenderers.getStringWidth(modules.firstOrNull()?.name.toString(), scale)

    override fun render() {
        var deltaY: Float = this.y
        ModuleManager.modules
            .filter { !modules.contains(it) && it.isDrawn }
            .sortedByDescending { FontRenderers.getStringWidth(it.name) }
            .forEach  { module ->
                if (!module.isEnabled) {
                    module.anim = 0.0f
                    modules.remove(module)
                    return@forEach
                }
                module.anim = MathUtils.lerp(module.anim, if (module.isEnabled) 1.0f else 0.0f, speed * 0.01)
                val moduleLength: Float = FontRenderers.getStringWidth(module.name) * scale
                val x: Float = this.x - (module.anim * moduleLength)
                if (!module.isEnabled && module.anim < 0.05f) {
                    modules.remove(module)
                }
                FontRenderers.drawString(module.name, x, deltaY, textColor, shadow, scale)
                deltaY += 12f * scale * module.anim;
            }
    }
}