package dev.exceptionteam.sakura.events

import dev.exceptionteam.sakura.events.impl.*
import dev.exceptionteam.sakura.utils.Wrapper
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer

abstract class AbstractClientContext {
    val mc = Wrapper.mc
    abstract val world: ClientLevel?
    abstract val player: LocalPlayer?
    abstract val connection: ClientPacketListener?
    abstract val playerController: MultiPlayerGameMode?
}

open class ClientContext: AbstractClientContext() {
    final override val world: ClientLevel? = mc.level
    final override val player: LocalPlayer? = mc.player
    final override val connection: ClientPacketListener? = mc.connection
    final override val playerController: MultiPlayerGameMode? = mc.gameMode

    inline operator fun <T> invoke(block: ClientContext.() -> T) = run(block)
}

open class NonNullContext internal constructor(
    override val world: ClientLevel,
    override val player: LocalPlayer,
    override val connection: ClientPacketListener,
    override val playerController: MultiPlayerGameMode
): AbstractClientContext() {

    inline operator fun <T> invoke(block: NonNullContext.() -> T) = run(block)

    companion object {

        var instance: NonNullContext? = null; private set

        init {
            listener<TickEvent.Pre>(alwaysListening = true) {
                update()
            }

            listener<DisconnectEvent>(alwaysListening = true) {
                reset()
            }

            listener<JoinLevelEvent>(alwaysListening = true) {
                reset()
            }
        }

        private fun update() {
            val world = Wrapper.world ?: return
            val player = Wrapper.player ?: return
            val connection = Wrapper.mc.connection ?: return
            val playerController = Wrapper.mc.gameMode ?: return

            instance = NonNullContext(world, player, connection, playerController)
        }

        private fun reset() {
            instance = null
        }

    }

}
