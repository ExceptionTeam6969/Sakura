package dev.exceptionteam.sakura.graphics

import org.lwjgl.opengl.GL45.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object GlHelper {

    // Disabled in core mode
    var blend by GLState(false) { if (it) glEnable(GL_BLEND) else glDisable(GL_BLEND) }
    var depth by GLState(false) { if (it) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST) }
    var cull by GLState(false) { if (it) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE) }
    var lineSmooth by GLState(false) { if (it) glEnable(GL_LINE_SMOOTH) else glDisable(GL_LINE_SMOOTH) }

}

class GLState<T>(valueIn: T, private val action: (T) -> Unit) : ReadWriteProperty<Any?, T> {

    private var value = valueIn

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            action.invoke(value)
        }
    }

}