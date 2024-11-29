package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

object AutoCrystal: Module(
    name = "auto-crystal",
    category = Category.COMBAT
) {

    private val page by setting("page", Page.GENERAL)

    // General
    private val targetRange by setting("target-range", 12f, 0f..24f, 0.5f) { page == Page.GENERAL }
    private val pauseWhileEating by setting("pause-while-eating", true) { page == Page.GENERAL }

    // Calculation
    private val predict by setting("predict", true) { page == Page.CALCULATION }
    private val predictTick by setting("predict-ticks", 4, 0..20) { page == Page.CALCULATION && predict }

    // Place
    private val placeDelay by setting("place-delay", 50, 0..1000, 5) { page == Page.PLACE }
    private val placeRange by setting("place-range", 4f, 0f..6f, 0.5f) { page == Page.PLACE }

    // Break
    private val breakDelay by setting("break-delay", 50, 0..1000, 5) { page == Page.BREAK }
    private val breakRange by setting("break-range", 4f, 0f..6f, 0.5f) { page == Page.BREAK }

    // Render
    private val color by setting("color", ColorRGB(255, 50, 50)) { page == Page.RENDER }

    private val renderer = ESPRenderer().apply { aFilled = 60; aOutline = 200 }

    init {

        nonNullListener<Render3DEvent> {
            renderer.add(player.blockPosition(), color)
            renderer.render(true)
        }

    }

    private enum class Page(override val key: CharSequence): TranslationEnum {
        GENERAL("general"),
        CALCULATION("calculation"),
        PLACE("place"),
        BREAK("break"),
        RENDER("render")
    }

}