package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.graphics.font.glyphs.FontGlyphs
import java.awt.Font

object FontRenderers {

    private val cnFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    ))

    // Comfortaa
    private val comFont = FontGlyphs(Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/comfortaa.ttf")
    ))

    val default = FontRenderer(comFont)
    val chinese = FontRenderer(cnFont)

}