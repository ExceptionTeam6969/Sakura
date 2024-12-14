package dev.exceptionteam.sakura.graphics

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL45.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object GlHelper {

    // Disabled in core mode
    private val blend0 = GlState(false) {
        if (it) {
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        } else
            glDisable(GL_BLEND)
    }
    var blend by blend0

    private val depth0 = GlState(false) { if (it) glEnable(GL_DEPTH_TEST) else glDisable(GL_DEPTH_TEST) }
    var depth by depth0

    private val cull0 = GlState(false) { if (it) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE) }
    var cull by cull0

    private val lineSmooth0 = GlState(false) { if (it) glEnable(GL_LINE_SMOOTH) else glDisable(GL_LINE_SMOOTH) }
    var lineSmooth by lineSmooth0

    private val scissor0 = GlState(false) { if (it) glEnable(GL_SCISSOR_TEST) else glDisable(GL_SCISSOR_TEST) }
    var scissor by scissor0

    var lineWidth: Float = 1.0f

    private val textures = TextureState()
    fun bindTexture(unit: Int, texture: Int) {
        if (textures.textures[unit] == texture) return
        textures.textures[unit] = texture
        glBindTextureUnit(unit, texture)
    }

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
        vertexArray0.forceSetValue(0)
        program0.forceSetValue(0)
        scissor0.forceSetValue(false)
    }

    fun syncWithMinecraft() {
        GlStateManager.TEXTURES.forEachIndexed { i, tex ->
            if (textures.textures[i] != tex.binding) {
                glActiveTexture(GL_TEXTURE0 + i)
                glBindTexture(GL_TEXTURE_2D, tex.binding)
                textures.textures[i] = tex.binding
            }
        }
        glActiveTexture(GL_TEXTURE0 + GlStateManager.activeTexture)

        if (GlStateManager.BLEND.mode.enabled != blend) GlStateManager.BLEND.mode.setEnabled(blend)
        if (GlStateManager.DEPTH.mode.enabled != depth) GlStateManager.DEPTH.mode.setEnabled(depth)
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

data class TextureState(
    val textures: Array<Int> = mutableListOf<Int>().apply {
        repeat(GlStateManager.TEXTURES.size + 1) { add(0) }
    }.toTypedArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextureState

        return textures.contentEquals(other.textures)
    }

    override fun hashCode(): Int {
        return textures.contentHashCode()
    }
}