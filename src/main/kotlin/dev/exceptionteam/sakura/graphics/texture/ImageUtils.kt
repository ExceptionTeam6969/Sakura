package dev.exceptionteam.sakura.graphics.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45.*
import java.awt.image.BufferedImage
import java.nio.IntBuffer

object ImageUtils {

    private const val DEFAULT_BUFFER_SIZE = 0x800000
    private var byteBuffer = BufferUtils.createByteBuffer(DEFAULT_BUFFER_SIZE) // Max 8 MB

    /**
     * Dynamic memory allocation
     */
    private fun putIntArray(intArray: IntArray): IntBuffer {
        byteBuffer.asIntBuffer().apply {
            clear()
            put(intArray)
            flip()
            return this
        }
    }

    /**
     * Load opengl texture from BufferedImage
     * @return OpenGL texture id
     */
    fun uploadImage(image: BufferedImage): Int {
        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        val pixels = IntArray(image.width * image.height)
        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)

        // BGRA TO RGBA
        for (index in 0 until (image.width * image.height)) {
            pixels[index] = pixels[index] and -0x1000000 or
                    (pixels[index] and 0x00FF0000 shr 16) or
                    (pixels[index] and 0x0000FF00) or
                    (pixels[index] and 0x000000FF shl 16)
        }

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            image.width,
            image.height,
            0,
            GL_BGRA,
            GL_UNSIGNED_BYTE,
            putIntArray(pixels)
        )

        glGenerateMipmap(GL_TEXTURE_2D)

        glBindTexture(GL_TEXTURE_2D, 0)

        return textureId
    }

}