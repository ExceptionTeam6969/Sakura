package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.features.modules.impl.client.CustomFont
import dev.exceptionteam.sakura.graphics.font.general.FontChunks
import dev.exceptionteam.sakura.graphics.font.general.GlyphChunk
import dev.exceptionteam.sakura.graphics.font.sparse.SparseFontGlyph
import java.awt.Font

class FontAdapter(
    val font: Font
) {
    val general = FontChunks(font)
    val sparse = SparseFontGlyph(font, CustomFont.fontSize.toFloat())

    fun getHeight(): Float = when (CustomFont.fontMode) {
        CustomFont.FontMode.GENERAL -> {
            general.getHeight()
        }

        CustomFont.FontMode.SPARSE -> {
            sparse.height
        }
    }

    fun getCharData(char: Char): CharData? {
        when (CustomFont.fontMode) {
            CustomFont.FontMode.GENERAL -> {
                if (general.canDisplay(char)) general.getChunk(char.code / GlyphChunk.CHUNK_SIZE).charData[char] else null
            }
            CustomFont.FontMode.SPARSE -> {
                return if (sparse.canDisplay(char)) sparse.getCharData(char) else null
            }
        }
        return null
    }

    fun canDisplay(char: Char): Boolean = when (CustomFont.fontMode) {
        CustomFont.FontMode.GENERAL -> {
            general.canDisplay(char)
        }
        CustomFont.FontMode.SPARSE -> {
            sparse.canDisplay(char)
        }
    }

}