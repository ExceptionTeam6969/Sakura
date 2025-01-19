package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.graphics.RenderSystem
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.graphics.utils.RenderUtils
import dev.exceptionteam.sakura.utils.threads.GameThreadUtils

object GraphicsManager {

    init {
        GameThreadUtils.runOnRenderThread {
            // Systems
            VertexBufferObjects
            RenderSystem
            // Fonts
            FontRenderers
            // Utils
            RenderUtils
        }
    }

}