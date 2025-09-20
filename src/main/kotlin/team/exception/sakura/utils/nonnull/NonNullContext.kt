package team.exception.sakura.utils.nonnull

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import team.exception.sakura.utils.Wrapper
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

abstract class AbstractClientContext {
    val mc = Wrapper.mc
    abstract val level: ClientLevel?
    abstract val player: LocalPlayer?
    abstract val connection: ClientPacketListener?
    abstract val gameMode: MultiPlayerGameMode?
}

open class ClientContext: AbstractClientContext() {
    final override val level: ClientLevel? = mc.level
    final override val player: LocalPlayer? = mc.player
    final override val connection: ClientPacketListener? = mc.connection
    final override val gameMode: MultiPlayerGameMode? = mc.gameMode

    inline operator fun <T> invoke(block: ClientContext.() -> T) = run(block)
}

open class NonNullContext internal constructor(
    override val level: ClientLevel,
    override val player: LocalPlayer,
    override val connection: ClientPacketListener,
    override val gameMode: MultiPlayerGameMode
): AbstractClientContext() {

    inline operator fun <T> invoke(block: NonNullContext.() -> T) = run(block)

    companion object {

        var instance: NonNullContext? = null; private set

        init {
            FORGE_BUS.addListener<PlayerTickEvent.Pre> { _ ->
                update()
            }

            FORGE_BUS.addListener<PlayerEvent.PlayerLoggedInEvent> { _ ->
                reset()
            }

            FORGE_BUS.addListener<PlayerEvent.PlayerRespawnEvent> { _ ->
                reset()
            }

            FORGE_BUS.addListener<PlayerEvent.PlayerLoggedOutEvent> { _ ->
                reset()
            }
        }

        private fun update() {
            instance = null

            val level = Wrapper.level ?: return
            val player = Wrapper.player ?: return
            val connection = Wrapper.mc.connection ?: return
            val gameMode = Wrapper.mc.gameMode ?: return

            instance = NonNullContext(level, player, connection, gameMode)
        }

        private fun reset() {
            instance = null
        }

    }

}

