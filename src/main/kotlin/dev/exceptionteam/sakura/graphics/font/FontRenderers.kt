package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import java.awt.Font

object FontRenderers {

    private const val FONT_SIZE = 16f

    private val cnFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    ).deriveFont(Font.BOLD, FONT_SIZE * 2f))

    // Comfortaa
    private val comFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/comfortaa.ttf")
    ).deriveFont(Font.BOLD, FONT_SIZE * 2f))

    val default = FontRenderer(comFont)
    val chinese = FontRenderer(cnFont)

}