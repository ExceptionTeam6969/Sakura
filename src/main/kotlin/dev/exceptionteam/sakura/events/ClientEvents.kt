package dev.exceptionteam.sakura.events

import dev.exceptionteam.sakura.events.impl.*
import dev.exceptionteam.sakura.utils.Wrapper
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.world.ClientWorld

abstract class AbstractClientContext {
    val mc = Wrapper.mc
    abstract val world: ClientWorld?
    abstract val player: ClientPlayerEntity?
    abstract val connection: ClientPlayNetworkHandler?
    abstract val playerController: ClientPlayerInteractionManager?
}

open class ClientContext: AbstractClientContext() {
    final override val world: ClientWorld? = mc.world
    final override val player: ClientPlayerEntity? = mc.player
    final override val connection: ClientPlayNetworkHandler? = mc.networkHandler
    final override val playerController: ClientPlayerInteractionManager? = mc.interactionManager

    inline operator fun <T> invoke(block: ClientContext.() -> T) = run(block)
}

open class NonNullContext internal constructor(
    override val world: ClientWorld,
    override val player: ClientPlayerEntity,
    override val connection: ClientPlayNetworkHandler,
    override val playerController: ClientPlayerInteractionManager
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

            listener<JoinWorldEvent>(alwaysListening = true) {
                reset()
            }
        }

        private fun update() {
            val world = Wrapper.world ?: return
            val player = Wrapper.player ?: return
            val connection = Wrapper.mc.networkHandler ?: return
            val playerController = Wrapper.mc.interactionManager ?: return

            instance = NonNullContext(world, player, connection, playerController)
        }

        private fun reset() {
            instance = null
        }

    }

}
