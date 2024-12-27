package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player

object TargetManager {

    private var targets: List<Entity>? = null

    init {

        nonNullListener<TickEvent.Update>(priority = Int.MAX_VALUE - 100, alwaysListening = true) {
            val tTargets = mutableListOf<Entity>()

            world.entitiesForRendering().forEach { ent ->
                if (ent.id == player.id) return@forEach
                if (ent.isSpectator) return@forEach
                if (!ent.isAlive) return@forEach
                if (ent !is Player && ent !is Mob) return@forEach

                tTargets.add(ent)
            }

            targets = if (tTargets.isNotEmpty()) tTargets else null
        }

    }

    /**
     * Get all targets in range of the player.
     *
     * @param range The range in blocks.
     * @return A list of targets in range.
     */
    fun getTargets(): List<Entity>? {
        return targets
    }

    fun NonNullContext.getTargets(): List<Entity>? = TargetManager.getTargets()

    /**
     * Get all players in range of the player.
     * @param range The range in blocks.
     * @return A list of players in range.
     */
    fun getTargetPlayers(): List<Player>? {
        return listOf(targets?.filter { it is Player } as Player)
    }

    fun NonNullContext.getTargetPlayers(): List<Player>? = TargetManager.getTargetPlayers()

    /**
     * Get the closest target in range of the player.
     * @param range The range in blocks.
     * @return The closest target in range.
     */
    fun NonNullContext.getTarget(range: Float): Entity? {
        targets?.let {
            it.sortedBy { it.distanceSqTo(player) }.forEach {
                if (it.distanceSqTo(player) >= range.sq) return@forEach
                return it
            }
        }
        return null
    }

    fun NonNullContext.getTargetPlayer(range: Float): Player? {
        targets?.let {
            it.sortedBy { it.distanceSqTo(player) }.forEach {
                if (it !is Player) return@forEach
                if (it.distanceSqTo(player) >= range.sq) return@forEach
                return it
            }
        }
        return null
    }

}