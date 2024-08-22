package dev.exceptionteam.sakura.graphics.font

import dev.exceptionteam.sakura.Sakura
import java.awt.Font

object FontRenderers {

    private val cnFont = Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    )

    // Comfortaa
    private val comFont = Font.createFont(
        Font.TRUETYPE_FONT,
        this.javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/font/chinese.ttf")
    )

    val default = FontRenderer(Glyphs(comFont), null)
    val chinese = FontRenderer(Glyphs(cnFont), default.font)

}