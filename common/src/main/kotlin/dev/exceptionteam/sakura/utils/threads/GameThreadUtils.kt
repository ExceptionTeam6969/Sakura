package dev.exceptionteam.sakura.utils.threads

import com.mojang.blaze3d.systems.RenderSystem

object GameThreadUtils {

    fun runOnRenderThread(func: ()->Unit) {
        if (RenderSystem.isOnRenderThread()) {
            func()
        } else {
            RenderSystem.recordRenderCall(func)
        }
    }

}