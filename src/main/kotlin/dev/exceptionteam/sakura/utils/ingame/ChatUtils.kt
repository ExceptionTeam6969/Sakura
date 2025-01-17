package dev.exceptionteam.sakura.utils.ingame

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.asm.IChatComponent
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@Suppress("UNUSED")
object ChatUtils {
    private const val SECTION = "\u00A7"
    const val BLACK = SECTION + "0"
    const val DARK_BLUE = SECTION + "1"
    const val DARK_GREEN = SECTION + "2"
    const val DARK_AQUA = SECTION + "3"
    const val DARK_RED = SECTION + "4"
    const val DARK_PURPLE = SECTION + "5"
    const val GOLD = SECTION + "6"
    const val GRAY = SECTION + "7"
    const val DARK_GRAY = SECTION + "8"
    const val BLUE = SECTION + "9"
    const val GREEN = SECTION + "a"
    const val AQUA = SECTION + "b"
    const val RED = SECTION + "c"
    const val LIGHT_PURPLE = SECTION + "d"
    const val YELLOW = SECTION + "e"
    const val WHITE = SECTION + "f"
    const val OBFUSCATED = SECTION + "k"
    const val BOLD = SECTION + "l"
    const val STRIKE_THROUGH = SECTION + "m"
    const val UNDER_LINE = SECTION + "n"
    const val ITALIC = SECTION + "o"
    const val RESET = SECTION + "r"
    const val COLOR_MSG = SECTION + "r"
    const val COLOR_KANJI = SECTION + "d"
    const val COLOR_WARN = SECTION + "6" + SECTION + "l"
    const val COLOR_ERROR = SECTION + "4" + SECTION + "l"
    const val COLOR_BRACKET = SECTION + "7"
    var msgCount = 0
    private var tempMsg: String? = null

    const val MAIN_COLOR = LIGHT_PURPLE

    private fun bracketBuilder(kanji: String): String {
        return "$RESET$COLOR_BRACKET[$RESET$kanji$COLOR_BRACKET] $RESET"
    }

    fun sendWarnMessage(message: String) {
        sendMessage(bracketBuilder(COLOR_WARN + "WARN") + RESET + COLOR_MSG + message)
    }

    fun sendErrorMessage(message: String) {
        sendMessage(bracketBuilder(COLOR_ERROR + "ERROR") + RESET + COLOR_MSG + message)
    }

    fun sendMessage(messageArray: Array<String?>) {
        for (message in messageArray) {
            if (message == null) continue
            sendMessage(message)
        }
    }

    fun sendMessage(message: String) {
        Minecraft.getInstance().gui?.let { gameHUD ->
            val text = Component.literal("${bracketBuilder(MAIN_COLOR + Sakura.NAME)} ${message.replace("§", SECTION)}")
            gameHUD.chat?.addMessage(text)
        }
    }

    fun sendNoSpamMessage(message: String) {
        val text = Component.literal("${bracketBuilder(MAIN_COLOR + Sakura.NAME)} ${message.replace("§", SECTION)}")
        (Minecraft.getInstance().gui.chat as IChatComponent).sakuraAddMessage(text, text.hashCode())
    }

    fun sendMessageWithID(message: String, id: Int) {
        val text = Component.literal("${bracketBuilder(MAIN_COLOR + Sakura.NAME)} ${message.replace("§", SECTION)}")
        (Minecraft.getInstance().gui.chat as IChatComponent).sakuraAddMessage(text, id)
    }
}
