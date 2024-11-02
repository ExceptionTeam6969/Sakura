package dev.exceptionteam.sakura.graphics

import org.lwjgl.opengl.GL45.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object GlHelper {

    // Disabled in core mode
    private val blend0 = GlState(false) { if (it) glEnable(GL_BLEND) else glDisable(GL_BLEND) }
    var blend by blend0

    private val depth0 = GlState(false) { if (it) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST) }
    var depth by depth0

    private val cull0 = GlState(false) { if (it) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE) }
    var cull by cull0

    private val lineSmooth0 = GlState(false) { if (it) glEnable(GL_LINE_SMOOTH) else glDisable(GL_LINE_SMOOTH) }
    var lineSmooth by lineSmooth0

    var lineWidth: Float = 1.0f

    private val texture0 = GlState(0) { glBindTextureUnit(0, it) }
    var texture by texture0

    private val vertexArray0 = GlState(0) { glBindVertexArray(it) }
    var vertexArray by vertexArray0

    private val program0 = GlState(0) { glUseProgram(it) }
    var program by program0

    fun reset() {
        blend0.forceSetValue(false)
        depth0.forceSetValue(false)
        cull0.forceSetValue(false)
        lineSmooth0.forceSetValue(false)
        lineWidth = 1.0f
        texture0.forceSetValue(0)
        vertexArray0.forceSetValue(0)
        program0.forceSetValue(0)
    }

}

class GlState<T>(valueIn: T, private val action: (T) -> Unit) : ReadWriteProperty<Any?, T> {

    private var value = valueIn

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            action.invoke(value)
        }
    }

    fun forceSetValue(value: T) {
        this.value = value
        action.invoke(value)
    }

}