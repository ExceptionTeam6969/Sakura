package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.graphics.RenderSystem
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.RenderUtilsTexture
import dev.exceptionteam.sakura.graphics.buffer.PMBuffer
import dev.exceptionteam.sakura.graphics.shader.FontRendererShader
import dev.exceptionteam.sakura.graphics.shader.PosColorShader2D
import dev.exceptionteam.sakura.managers.AbstractManager

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        // System
        RenderSystem
        PMBuffer
        // Utils
        RenderUtils2D
        RenderUtilsTexture
        // Shaders
        FontRendererShader
        PosColorShader2D
    }

}