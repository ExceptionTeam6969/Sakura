package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import org.lwjgl.glfw.GLFW

object ChatInfo: Module(
    name = "ChatInfo",
    description = "Send information when modules toggling",
    category = Category.CLIENT,
    defaultBind = GLFW.GLFW_KEY_U,
    defaultEnable = true
)