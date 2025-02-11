package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object ChatNotification: Module(
    name = "chat-notification",
    category = Category.CLIENT,
    defaultEnable = true,
)