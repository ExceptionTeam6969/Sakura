package dev.exceptionteam.sakura.graphics.font.glyphs

import java.awt.Font

class FontGlyphs(
    val font: Font
) {

    private val glyphs = hashMapOf<Char, Glyph>()

    fun getGlyph(char: Char): Glyph {
        glyphs[char]?.let { return it }
        val glyph = Glyph(font, char)
        glyphs[char] = glyph
        return glyph
    }

    fun destroy() {
        glyphs.values.forEach { it.destroy() }
    }

    fun canDisplay(char: Char): Boolean {
        return font.canDisplay(char)
    }

}