package dev.exceptionteam.sakura.managers.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.events.impl.QuitGameEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.features.settings.*
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.utils.resources.checkFile
import dev.exceptionteam.sakura.utils.threads.IOScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files

object ConfigManager {

    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()!!

    init {
        loadAll()

        listener<QuitGameEvent>(alwaysListening = true) {
            saveAll()
        }
    }

    fun loadAll() = runCatching {
        runBlocking {
            loadModule()
            loadFriends()
        }
    }.onFailure { Sakura.logger.error("Failed To Load Config!") }

    fun saveAll() = runCatching {
        runBlocking {
            saveModule()
            saveFriends()
        }
    }.onFailure { Sakura.logger.error("Failed To Save Config!") }

    /* Load */
    fun loadModule() {
        ModuleManager.modules.forEach { module ->
            IOScope.launch {
                val moduleFile = File("${Sakura.DIRECTORY}/modules/${module.name.key}.json")
                moduleFile.checkFile()
                val moduleJson = async { gsonPretty.fromJson(moduleFile.readText(), JsonObject::class.java) }.await()

                if (module is HUDModule) {
                    try {
                        module.x = moduleJson.get("x").asFloat
                        module.y = moduleJson.get("y").asFloat
                    } catch (_: Exception) {
                        Sakura.logger.warn("Failed to load position of hud module ${module.name.key}")
                    }
                }

                module.settings.forEach { setting ->
                    try {
                        when (setting.key.key) {
                            "toggle" -> {
                                if (setting is BooleanSetting) {
                                    if (moduleJson.get("toggle").asBoolean) module.enable()
                                    else module.disable()
                                }
                                return@forEach
                            }

                            "key-bind" -> {
                                if (setting is KeyBindSetting) setting.value =
                                    KeyBind(KeyBind.Type.KEYBOARD, moduleJson.get("key-bind").asInt)
                                return@forEach
                            }

                            else -> {}
                        }

                        when (setting) {
                            is IntSetting -> setting.value = moduleJson.get(setting.key.key).asInt
                            is LongSetting -> setting.value = moduleJson.get(setting.key.key).asLong
                            is FloatSetting -> setting.value = moduleJson.get(setting.key.key).asFloat
                            is DoubleSetting -> setting.value = moduleJson.get(setting.key.key).asDouble
                            is BooleanSetting -> setting.value = moduleJson.get(setting.key.key).asBoolean
                            is KeyBindSetting -> setting.value =
                                KeyBind(KeyBind.Type.KEYBOARD, moduleJson.get(setting.key.key).asInt)

                            is ColorSetting -> setting.value = ColorRGB(moduleJson.get(setting.key.key).asInt)
                            is EnumSetting<*> -> setting.setWithName(moduleJson.get(setting.key.key).asString)
                            else -> {}
                        }
                    } catch (_: Exception) {
                        Sakura.logger.warn("Failed to load setting ${setting.key.key} in module ${module.name.key}")
                    }
                }
            }
        }
    }

    fun loadFriends() {
        IOScope.launch {
            try {
                val friendFile = File("${Sakura.DIRECTORY}/friends.json")
                friendFile.checkFile()
                val friendJson = Gson().fromJson(
                    String(Files.readAllBytes(friendFile.toPath()), StandardCharsets.UTF_8),
                    ArrayList::class.java
                ) ?: return@launch
                FriendManager.friends.clear()
                friendJson.forEach {
                    FriendManager.friends.add(it.toString())
                }
            } catch (e: IOException) {
                Sakura.logger.error("Error while loading friends!")
                e.printStackTrace()
            }
        }
    }

    /* Save */
    fun saveModule() {
        ModuleManager.modules.forEach { module ->
            val moduleFile = File("${Sakura.DIRECTORY}/modules/${module.name.key}.json")
            moduleFile.checkFile()
            val moduleJson = JsonObject()

            if (module is HUDModule) {
                moduleJson.addProperty("x", module.x)
                moduleJson.addProperty("y", module.y)
            }

            module.settings.forEach { setting ->
                when (setting.key.key) {
                    "toggle" -> {
                        moduleJson.addProperty("toggle", setting.value as Boolean)
                        return@forEach
                    }

                    "key-bind" -> {
                        moduleJson.addProperty("key-bind", (setting.value as KeyBind).keyCode)
                        return@forEach
                    }

                    else -> {}
                }

                when (setting) {
                    is IntSetting -> moduleJson.addProperty(setting.key.key, setting.value)
                    is LongSetting -> moduleJson.addProperty(setting.key.key, setting.value)
                    is FloatSetting -> moduleJson.addProperty(setting.key.key, setting.value)
                    is DoubleSetting -> moduleJson.addProperty(setting.key.key, setting.value)
                    is BooleanSetting -> moduleJson.addProperty(setting.key.key, setting.value)
                    is KeyBindSetting -> moduleJson.addProperty(setting.key.key, setting.value.keyCode)
                    is ColorSetting -> moduleJson.addProperty(setting.key.key, setting.value.rgba)
                    is EnumSetting<*> -> moduleJson.addProperty(setting.key.key, setting.value.name)
                    else -> {}
                }
            }

            val saveJson = PrintWriter(OutputStreamWriter(FileOutputStream(moduleFile), StandardCharsets.UTF_8))
            saveJson.println(gsonPretty.toJson(moduleJson))
            saveJson.close()
        }
    }

    fun saveFriends() {
        val friendFile = File("${Sakura.DIRECTORY}/friends.json")
        friendFile.checkFile()
        Files.write(
            friendFile.toPath(), Gson().toJson(FriendManager.friends).toByteArray(
                StandardCharsets.UTF_8
            )
        )
    }

}