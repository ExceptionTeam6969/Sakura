package dev.exceptionteam.sakura.graphics.font

import java.awt.Font

class FontChunks(
    private val font: Font,
) {

    private val chunks = mutableMapOf<Int, GlyphChunk>()

    fun getChunk(id: Int): GlyphChunk =
        chunks.getOrPut(id) {
            var chunk = getChunkFromCache(id)
            if (chunk == null) {
                chunk = GlyphChunk(id, font, (FontRenderers.FONT_SIZE * 2).toInt())
                // TODO: Write to cache
            }
            chunk
        }

    private fun getChunkFromCache(id: Int): GlyphChunk? {
        // TODO: Read from cache
        return null
    }

    fun canDisplay(char: Char): Boolean = font.canDisplay(char)

    fun getHeight(): Float {
        val chunk = getChunk(0)
        val height = chunk.charData[Char(0)]?.height ?: 0f
        return height.toFloat()
    }

}