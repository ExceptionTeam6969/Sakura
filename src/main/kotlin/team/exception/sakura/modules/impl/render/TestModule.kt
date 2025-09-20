package team.exception.sakura.modules.impl.render

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RenderGuiEvent
import org.lwjgl.glfw.GLFW
import team.exception.sakura.modules.AbstractModule
import team.exception.sakura.modules.Category
import team.exception.sakura.utils.nonnull.runSafe

object TestModule: AbstractModule(
    name = "test-module",
    category = Category.RENDER,
    defaultKey = GLFW.GLFW_KEY_Y
) {

    @SubscribeEvent
    fun onRender2D(event: RenderGuiEvent.Post) = runSafe {
        event.guiGraphics.drawString(mc.font, "Hello, world!", 10, 10, 0xFFFFFFFF.toInt())
    }

}