package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers

class ModuleComponent(
    val module: AbstractModule,
    x: Float, y: Float, width: Float, height: Float
): AbstractComponent(x, y, width, height) {

    override fun render() {
        FontRenderers.default.drawString(
            module.name.translation,
            x + 5f, y + 10f,
            ColorRGB.BLACK
        )
        super.render()
    }

}