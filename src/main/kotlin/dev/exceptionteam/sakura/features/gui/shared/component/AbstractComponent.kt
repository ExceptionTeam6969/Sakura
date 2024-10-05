package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.utils.control.MouseButtonType

abstract class AbstractComponent(
    x: Float,
    y: Float,
    open var width: Float,
    open var height: Float
) {
    var x: Float = x; private set
    var y: Float = y; private set

    var mouseX: Float = 0f
    var mouseY: Float = 0f
    private var isHovering: Boolean = false

    open fun render() {}

    open fun mouseClicked(type: MouseButtonType): Boolean = false

    open fun mouseReleased(type: MouseButtonType): Boolean = false

    open fun keyPressed(key: KeyBind) {}

    open fun updatePosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    open fun checkHovering(): Boolean {
        isHovering = mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height
        return isHovering
    }

}