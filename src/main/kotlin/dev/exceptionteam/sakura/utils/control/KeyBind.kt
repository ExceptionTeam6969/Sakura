package dev.exceptionteam.sakura.utils.control

import org.lwjgl.glfw.GLFW
import java.io.Serializable

data class KeyBind @JvmOverloads constructor(
    var type: Type = Type.KEYBOARD,
    var keyCode: Int = -1,
    var scanCode: Int = 1
) : Serializable {
    val keyName: String
        get() = getKeyName(keyCode, scanCode)

    companion object {
        fun getKeyName(key: Int, scanCode: Int): String {
            return when (key) {
                GLFW.GLFW_KEY_UNKNOWN -> "None"
                GLFW.GLFW_KEY_ESCAPE -> "Esc"
                GLFW.GLFW_KEY_GRAVE_ACCENT -> "Grave"
                GLFW.GLFW_KEY_WORLD_1 -> "World 1"
                GLFW.GLFW_KEY_WORLD_2 -> "World 2"
                GLFW.GLFW_KEY_PRINT_SCREEN -> "Print Screen"
                GLFW.GLFW_KEY_PAUSE -> "Pause"
                GLFW.GLFW_KEY_INSERT -> "Insert"
                GLFW.GLFW_KEY_DELETE -> "Delete"
                GLFW.GLFW_KEY_HOME -> "Home"
                GLFW.GLFW_KEY_PAGE_UP -> "Page Up"
                GLFW.GLFW_KEY_PAGE_DOWN -> "Page Down"
                GLFW.GLFW_KEY_END -> "End"
                GLFW.GLFW_KEY_TAB -> "Tab"
                GLFW.GLFW_KEY_LEFT_CONTROL -> "LControl"
                GLFW.GLFW_KEY_RIGHT_CONTROL -> "RControl"
                GLFW.GLFW_KEY_LEFT_ALT -> "LAlt"
                GLFW.GLFW_KEY_RIGHT_ALT -> "RAlt"
                GLFW.GLFW_KEY_LEFT_SHIFT -> "LShift"
                GLFW.GLFW_KEY_RIGHT_SHIFT -> "RShift"
                GLFW.GLFW_KEY_UP -> "Up"
                GLFW.GLFW_KEY_DOWN -> "Down"
                GLFW.GLFW_KEY_LEFT -> "Left"
                GLFW.GLFW_KEY_RIGHT -> "Right"
                GLFW.GLFW_KEY_APOSTROPHE -> "Apostrophe"
                GLFW.GLFW_KEY_BACKSPACE -> "Backspace"
                GLFW.GLFW_KEY_CAPS_LOCK -> "Caps Lock"
                GLFW.GLFW_KEY_MENU -> "Menu"
                GLFW.GLFW_KEY_LEFT_SUPER -> "LSuper"
                GLFW.GLFW_KEY_RIGHT_SUPER -> "RSuper"
                GLFW.GLFW_KEY_ENTER -> "Enter"
                GLFW.GLFW_KEY_KP_ENTER -> "Numpad Enter"
                GLFW.GLFW_KEY_NUM_LOCK -> "NumLock"
                GLFW.GLFW_KEY_SPACE -> "Space"
                GLFW.GLFW_KEY_F1 -> "F1"
                GLFW.GLFW_KEY_F2 -> "F2"
                GLFW.GLFW_KEY_F3 -> "F3"
                GLFW.GLFW_KEY_F4 -> "F4"
                GLFW.GLFW_KEY_F5 -> "F5"
                GLFW.GLFW_KEY_F6 -> "F6"
                GLFW.GLFW_KEY_F7 -> "F7"
                GLFW.GLFW_KEY_F8 -> "F8"
                GLFW.GLFW_KEY_F9 -> "F9"
                GLFW.GLFW_KEY_F10 -> "F10"
                GLFW.GLFW_KEY_F11 -> "F11"
                GLFW.GLFW_KEY_F12 -> "F12"
                GLFW.GLFW_KEY_F13 -> "F13"
                GLFW.GLFW_KEY_F14 -> "F14"
                GLFW.GLFW_KEY_F15 -> "F15"
                GLFW.GLFW_KEY_F16 -> "F16"
                GLFW.GLFW_KEY_F17 -> "F17"
                GLFW.GLFW_KEY_F18 -> "F18"
                GLFW.GLFW_KEY_F19 -> "F19"
                GLFW.GLFW_KEY_F20 -> "F20"
                GLFW.GLFW_KEY_F21 -> "F21"
                GLFW.GLFW_KEY_F22 -> "F22"
                GLFW.GLFW_KEY_F23 -> "F23"
                GLFW.GLFW_KEY_F24 -> "F24"
                GLFW.GLFW_KEY_F25 -> "F25"
                else -> {
                    GLFW.glfwGetKeyName(key, scanCode) ?: "Unknown"
                }
            }
        }
    }

    fun valueToString(): String = "${type.name}:$keyCode:$scanCode"
    fun valueFromString(string: String) {
        val split = string.split(":")
        this.type = Type.valueOf(split[0])
        this.keyCode = split[1].toInt()
        this.scanCode = split[2].toInt()
    }

    enum class Type {
        KEYBOARD,
        MOUSE
    }
}