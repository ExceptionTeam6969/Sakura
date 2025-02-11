package dev.exceptionteam.sakura

import dev.exceptionteam.sakura.addons.AddonManager
import net.fabricmc.api.ClientModInitializer

class SakuraMod: ClientModInitializer {
    override fun onInitializeClient() {
        AddonManager
    }
}