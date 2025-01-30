package dev.exceptionteam.sakura.features.modules.impl.hud

import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.ingame.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerInfo

object Ping: HUDModule(
    name = "ping",
) {
    private val textColor by setting("text-color", ColorRGB.WHITE)
    private val shadow by setting("shadow", true)
    private val scale by setting("scale", 1.0f, 0.5f..2.0f)

    private val mc get() = Minecraft.getInstance()
    var text: String? = null

    override var height: Float = 12f
        get() = (12f * scale)

    override var width: Float = 50f
        get() = FontRenderers.getStringWidth(text ?: "-1 ms", scale)

    override fun render() {
        text = "${getPing()} ms"
        FontRenderers.drawString(text!!, x, y, textColor, shadow, scale)
    }

    private fun getPing(): Int {
        if (mc.connection == null) return -1
        val uuid = mc.player?.uuid ?: return -1
        val playerInfo: PlayerInfo? = mc.connection!!.getPlayerInfo(uuid)
        return playerInfo?.latency ?: -1
    }

}