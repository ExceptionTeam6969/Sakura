package dev.exceptionteam.sakura.addons

import dev.exceptionteam.sakura.events.impl.RegisterLanguageEvent
import dev.exceptionteam.sakura.events.impl.RegisterModuleEvent
import dev.exceptionteam.sakura.events.listener
import net.fabricmc.loader.api.FabricLoader

object AddonManager {

    private val addons = mutableListOf<SakuraAddon>()

    init {

        FabricLoader.getInstance().getEntrypointContainers("sakura", SakuraAddon::class.java).forEach { entrypoint ->
            val addon: SakuraAddon

            try {
                addon = entrypoint.entrypoint
            } catch (e: Exception) {
                e.printStackTrace()
                return@forEach
            }

            addons.add(addon)
        }

        addons.forEach { addon ->
            addon.onInitialize()
        }

        listener<RegisterModuleEvent>(alwaysListening = true) { event ->
            addons.forEach { addon ->
                event.modules.addAll(addon.getModules())
            }
        }

        listener<RegisterLanguageEvent>(alwaysListening = true) { event ->
            addons.forEach { addon ->
                val languageData = addon.getLanguageData()
                event.map.putAll(languageData[event.language] ?: return@forEach)
            }
        }
    }

}