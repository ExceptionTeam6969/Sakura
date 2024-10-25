package dev.exceptionteam.sakura.utils.ingame

import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.asm.IChatHud
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

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
    const val colorMSG = SECTION + "r"
    const val colorKANJI = SECTION + "d"
    const val colorWarn = SECTION + "6" + SECTION + "l"
    const val colorError = SECTION + "4" + SECTION + "l"
    const val colorBracket = SECTION + "7"
    var msgCount = 0
    private var tempMsg: String? = null

    private fun bracketBuilder(kanji: String): String {
        return "$RESET$colorBracket[$RESET$kanji$colorBracket] $RESET"
    }

    fun sendWarnMessage(message: String) {
        sendMessage(bracketBuilder(colorWarn + "WARN") + RESET + colorMSG + message)
    }

    fun sendErrorMessage(message: String) {
        sendMessage(bracketBuilder(colorError + "ERROR") + RESET + colorMSG + message)
    }

    fun sendMessage(messageArray: Array<String?>) {
        for (message in messageArray) {
            if (message == null) continue
            sendMessage(message)
        }
    }

    fun sendMessage(message: String) {
        MinecraftClient.getInstance().inGameHud?.let { gameHUD ->
            val text = Text.literal("${bracketBuilder(AQUA + Sakura.NAME)} ${message.replace("§", SECTION)}")
            gameHUD.chatHud?.addMessage(text)
        }
    }

    fun sendNoSpamMessage(message: String) {
        val text = Text.literal("${bracketBuilder(AQUA + Sakura.NAME)} ${message.replace("§", SECTION)}")
        (MinecraftClient.getInstance().inGameHud.chatHud as IChatHud).sakuraAddMessage(text, text.hashCode())
    }

    fun sendMessageWithID(message: String, id: Int) {
        val text = Text.literal("${bracketBuilder(AQUA + Sakura.NAME)} ${message.replace("§", SECTION)}")
        (MinecraftClient.getInstance().inGameHud.chatHud as IChatHud).sakuraAddMessage(text, id)
    }
}
