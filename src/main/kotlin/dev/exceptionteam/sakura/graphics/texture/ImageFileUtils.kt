package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.utils.memory.createDirectByteBuffer
import dev.exceptionteam.sakura.utils.resources.Resource
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import org.lwjgl.opengl.GL45.*
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

object ImageFileUtils {

    private const val DEFAULT_BUFFER_SIZE = 0x800000
    private var byteBuffer = createDirectByteBuffer(DEFAULT_BUFFER_SIZE) // Max 8 MB
    private val reallocateTimer = TimerUtils()

    /**
     * Dynamic memory allocation
     */
    private fun putByteArray(byteArray: ByteArray): ByteBuffer {
        if (byteArray.size > byteBuffer.capacity()) {
            byteBuffer.clear()
            byteBuffer = createDirectByteBuffer(byteArray.size)
            reallocateTimer.reset()
        } else if (
            byteBuffer.capacity() > DEFAULT_BUFFER_SIZE
            && byteArray.size < DEFAULT_BUFFER_SIZE
            && reallocateTimer.passed(1000)
        ) {
            byteBuffer.clear()
            byteBuffer = createDirectByteBuffer(DEFAULT_BUFFER_SIZE)
        }

        byteBuffer.apply {
            clear()
            put(byteArray)
            flip()
            return this
        }
    }


    /**
     * Loads a texture from a file and returns a texture object.
     * @param path The path to the file.
     * @return The texture object.
     */
    fun loadTextureFromFile(path: String): Texture {
        val tex = Texture()

        val width = IntArray(1)
        val height = IntArray(1)
        val channels = IntArray(1)

        val imageData = STBImage.stbi_load(path, width, height, channels, 4)

        if (imageData == null) {
            throw Exception("Failed to load image: $path")
        }

        tex.width = width[0]
        tex.height = height[0]

        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(tex.id, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTextureStorage2D(tex.id, 1, GL_RGBA8, width[0], height[0])

        glTextureSubImage2D(tex.id, 0, 0, 0, width[0], height[0], GL_RGBA, GL_UNSIGNED_BYTE, imageData)

        STBImage.stbi_image_free(imageData)

        return tex
    }

    /**
     * Loads a texture from a byte buffer and returns a texture object.
     * @param bytes The byte buffer.
     * @return The texture object.
     */
    fun loadTextureFromFileBytes(bytes: ByteBuffer): Texture {
        val tex = Texture()

        val width = IntArray(1)
        val height = IntArray(1)
        val channels = IntArray(1)

        val imageData = STBImage.stbi_load_from_memory(bytes, width, height, channels, 4)

        if (imageData == null) {
            throw Exception("Failed to load image from bytes")
        }

        tex.width = width[0]
        tex.height = height[0]

        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTextureParameteri(tex.id, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(tex.id, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTextureStorage2D(tex.id, 1, GL_RGBA8, width[0], height[0])

        glTextureSubImage2D(tex.id, 0, 0, 0, width[0], height[0], GL_RGBA, GL_UNSIGNED_BYTE, imageData)

        STBImage.stbi_image_free(imageData)

        return tex
    }

    fun loadTextureFromResource(res: Resource): Texture {
        return loadTextureFromFileBytes(putByteArray(res.byteArr))
    }
}