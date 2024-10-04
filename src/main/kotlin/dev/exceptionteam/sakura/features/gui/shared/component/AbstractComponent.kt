package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.utils.control.MouseButtonType

abstract class AbstractComponent(
    x: Float,
    y: Float,
    width: Float,
    height: Float
) {
    var x: Float = x; private set
    var y: Float = y; private set
    var width: Float = width; private set
    var height: Float = height; private set

    var mouseX: Float = 0f
    var mouseY: Float = 0f
    private var isHovering: Boolean = false

    open fun render() {}

    open fun mouseClicked(type: MouseButtonType) {}

    open fun mouseReleased(type: MouseButtonType) {}

    open fun keyPressed(key: KeyBind) {}

    fun updatePosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun checkHovering(): Boolean {
        isHovering = mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height
        return isHovering
    }

}