package dev.exceptionteam.sakura.utils.control

import org.lwjgl.glfw.GLFW

object KeyUtils {
    private val map = mutableMapOf<String, Int>()

    init {
        val prefix = "GLFW_KEY_"
        GLFW::class.java.declaredFields
            .filter { it.name.startsWith(prefix) }
            .forEach {
                map[it.name.removePrefix(prefix)] = it.get(null) as Int
            }
    }

    fun parseToKeyCode(keyName: String): Int {
        return map[keyName] ?: -1
    }
}