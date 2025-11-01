package team.exception.sakura.graphics

import team.exception.sakura.graphics.geek2.G2Device
import team.exception.sakura.graphics.gl.GlDevice

object G2RenderSystem {

    val backend: Backends = Backends.OPENGL

    val device: G2Device = when (backend) {
        Backends.OPENGL -> GlDevice()
    }

    enum class Backends {
        OPENGL,
    }

}