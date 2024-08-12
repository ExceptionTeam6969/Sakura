package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.graphics.gl.RenderSystem
import dev.exceptionteam.sakura.graphics.gl.buffer.PMBuffer
import dev.exceptionteam.sakura.managers.AbstractManager

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        RenderSystem
        PMBuffer
    }

}