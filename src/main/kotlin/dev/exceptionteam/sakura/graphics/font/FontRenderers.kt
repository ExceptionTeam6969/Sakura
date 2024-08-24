package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import java.awt.Font

object FontRenderers {

    private val cnFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    ).deriveFont(Font.BOLD, 15f))

    // Comfortaa
    private val comFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/comfortaa.ttf")
    ).deriveFont(Font.BOLD, 15f))

    val default = FontRenderer(comFont)
    val chinese = FontRenderer(cnFont)

}