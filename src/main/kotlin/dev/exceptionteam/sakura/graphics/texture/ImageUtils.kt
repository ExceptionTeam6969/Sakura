package dev.exceptionteam.sakura.graphics.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.awt.image.BufferedImage
import java.nio.IntBuffer

object ImageUtils {
    private const val BUFFER_SIZE = 1 * 1024 * 1024     // 1MB
    private var byteBuffer = BufferUtils.createByteBuffer(BUFFER_SIZE)

    private fun putIntArray(intArray: IntArray): IntBuffer {
        byteBuffer.asIntBuffer().apply {
            clear()
            put(intArray)
            flip()
            return this
        }
    }

    fun loadImageToTexture(image: BufferedImage): Texture {
        val tex = Texture()

        val width = image.width
        val height = image.height

        val pixels = IntArray(width * height)
        image.getRGB(0, 0, width, height, pixels, 0, width)

        for (index in 0 until (image.width * image.height)) {
            pixels[index] = pixels[index] and -0x1000000 or
                    (pixels[index] and 0x00FF0000 shr 16) or
                    (pixels[index] and 0x0000FF00) or
                    (pixels[index] and 0x000000FF shl 16)
        }

        glTextureStorage2D(tex.id, 1, GL_RGBA8, width, height)
        glTextureSubImage2D(tex.id, 0, 0, 0, width, height,
            GL_RGBA, GL_UNSIGNED_BYTE, putIntArray(pixels))

        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(tex.id, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glGenerateTextureMipmap(tex.id)

        return tex
    }

}