package dev.exceptionteam.sakura.graphics.font.glyphs

import java.awt.Font

class FontGlyphs(
    private val font: Font
) {

    private val glyphs = hashMapOf<Char, Glyph>()

    fun getGlyph(char: Char): Glyph {
        return glyphs.getOrPut(char) { Glyph(font, char) }
    }

    fun destroy() {
        glyphs.values.forEach { it.destroy() }
    }

    fun canDisplay(char: Char): Boolean = font.canDisplay(char)

}