package dev.exceptionteam.sakura.graphics

import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL45.*

class ScissorBox(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0
) {

    constructor(x: Float, y: Float, width: Float, height: Float):
        this(x.toInt(), y.toInt(), width.toInt(), height.toInt())

    /**
     * Updates the scissor box.
     */
    fun update(x: Int, y: Int, width: Int, height: Int) {
        this.x = x; this.y = y
        this.width = width; this.height = height
    }

    fun update(x: Float, y: Float, width: Float, height: Float) {
        update(x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    /**
     * Draws the scissor box and executes the given function.
     * @param func The function to execute.
     */
    fun draw(func: () -> Unit) {
        GlHelper.scissor = true
        val factor = mc.window.guiScale
        glScissor((x * factor).toInt(), (mc.window.height - (y + height) * factor).toInt(),
            (width * factor).toInt(), (height * factor).toInt())
        func()
        GlHelper.scissor = false
    }

    /**
     * Updates and draws the scissor box and executes the given function.
     * @param func The function to execute.
     * @see update
     * @see draw
     */
    fun updateAndDraw(x: Int, y: Int, width: Int, height: Int, func: () -> Unit) {
        update(x, y, width, height)
        draw(func)
    }

    fun updateAndDraw(x: Float, y: Float, width: Float, height: Float, func: () -> Unit) {
        update(x, y, width, height)
        draw(func)
    }

    companion object {
        private val mc get() = Minecraft.getInstance()
    }

}