package dev.exceptionteam.sakura.features.modules

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.translation.TranslationString

abstract class HUDModule(
    name: String,
    val width: Float,
    val height: Float,
    defaultEnable: Boolean = false,
    alwaysEnable: Boolean = false,
    defaultBind: Int = -1
): AbstractModule(
    TranslationString("modules", name),
    Category.HUD,
    TranslationString("modules", "description"),
    defaultEnable,
    alwaysEnable,
    defaultBind
) {

    init {

        nonNullListener<Render2DEvent> {
            render()
        }
        
    }

    var x: Float = 0.0f
    var y: Float = 0.0f

    abstract fun render()

}