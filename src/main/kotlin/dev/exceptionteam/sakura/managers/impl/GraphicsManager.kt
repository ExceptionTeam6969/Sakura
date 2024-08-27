package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.graphics.RenderSystem
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.RenderUtilsTexture
import dev.exceptionteam.sakura.graphics.buffer.PersistentMappedVBO
import dev.exceptionteam.sakura.graphics.buffer.VertexBufferObjects
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.graphics.shader.PosTexShader2D
import dev.exceptionteam.sakura.graphics.shader.PosColorShader2D
import dev.exceptionteam.sakura.managers.AbstractManager
import dev.exceptionteam.sakura.utils.threads.GameThreadUtils

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        GameThreadUtils.runOnRenderThread {
            // Systems
            VertexBufferObjects
            RenderSystem
            // Fonts
            FontRenderers
            // Utils
            RenderUtils2D
            RenderUtilsTexture
            // Shaders
            PosTexShader2D
            PosColorShader2D
        }
    }

}