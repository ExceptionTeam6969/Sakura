package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.managers.AbstractManager
import dev.exceptionteam.sakura.utils.graphics.gl.buffer.PersistentMappedVBO
import dev.exceptionteam.sakura.utils.graphics.gl.shader.ShaderLoader

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        PersistentMappedVBO
        ShaderLoader
    }

}