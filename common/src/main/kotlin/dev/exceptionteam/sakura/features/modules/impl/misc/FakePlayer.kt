package dev.exceptionteam.sakura.features.modules.impl.misc

import com.mojang.authlib.GameProfile
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.threads.runSafe
import net.minecraft.client.player.RemotePlayer
import net.minecraft.world.entity.Entity
import java.util.UUID

object FakePlayer: Module(
    name = "fake-player",
    category = Category.MISC,
) {

    private val health by setting("health", 20f, 0f..36f)
    private var fakePlayer: RemotePlayer? = null

    init {

        onEnable {
            runSafe {
                fakePlayer = RemotePlayer(
                    world,
                    GameProfile(UUID.fromString("60569353-f22b-42da-b84b-d706a65c5ddf"), "FakePlayer")
                )
                fakePlayer?.let { fakePlayer ->
                    fakePlayer.copyPosition(player)
                    for (potionEffect in player.activeEffectsMap) {
                        fakePlayer.addEffect(potionEffect.value)
                    }
                    fakePlayer.health = health
                    fakePlayer.inventory.replaceWith(player.inventory)
                    fakePlayer.yRot = player.yRot
                    world.addEntity(fakePlayer)
                }
            }
        }

        onDisable {
            runSafe {
                fakePlayer?.let {
                    world.removeEntity(it.id, Entity.RemovalReason.KILLED)
                }
            }
        }

    }

}