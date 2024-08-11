package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.graphics.Render2DUtils
import dev.exceptionteam.sakura.managers.AbstractManager
import dev.exceptionteam.sakura.graphics.gl.buffer.PersistentMappedVBO

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        PersistentMappedVBO
        Render2DUtils
    }

}