package team.exception.sakura.graphics

import org.lwjgl.opengl.GL41.*
import team.exception.sakura.graphics.geek2.G2Device
import team.exception.sakura.graphics.gl.GlDevice

object G2RenderSystem {

    val backend: Backends = Backends.OPENGL

    val gpuType: GpuTypes = pickGpuType()

    private fun pickGpuType(): GpuTypes {
        when (backend) {
            Backends.OPENGL -> {
                val glVendor = glGetString(GL_VENDOR) ?: return GpuTypes.OTHER
                return when {
                    glVendor.contains("Intel") -> GpuTypes.INTEL
                    glVendor.contains("AMD") -> GpuTypes.AMD
                    glVendor.contains("NVIDIA") -> GpuTypes.NVIDIA
                    glVendor.contains("Apple") -> GpuTypes.APPLE
                    else -> GpuTypes.OTHER
                }
            }
        }
    }

    val device: G2Device = when (backend) {
        Backends.OPENGL -> GlDevice()
    }

    enum class Backends {
        OPENGL,
    }

    enum class GpuTypes {
        INTEL,
        AMD,
        NVIDIA,
        APPLE,
        OTHER,
    }

}