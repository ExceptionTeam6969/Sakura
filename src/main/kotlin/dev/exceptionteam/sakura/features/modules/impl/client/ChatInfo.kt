package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import org.lwjgl.glfw.GLFW

object ChatInfo: Module(
    name = "chat-info",
    category = Category.CLIENT,
    defaultEnable = true,
)