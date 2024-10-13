package dev.exceptionteam.sakura.managers.impl

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.exceptionteam.sakura.Sakura
import dev.exceptionteam.sakura.events.impl.QuitGameEvent
import dev.exceptionteam.sakura.events.listener
import dev.exceptionteam.sakura.features.settings.*
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.utils.resources.checkFile
import dev.exceptionteam.sakura.utils.threads.IOScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

object ConfigManager {

    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()!!

    init {
        loadAll()

        listener<QuitGameEvent>(alwaysListening = true) {
            saveAll()
        }
    }

    fun loadAll() {
        try {
            loadModule()
        } catch (_: Exception) {
            Sakura.logger.error("Failed to load config")
        }
    }

    fun saveAll() {
        saveModule()
    }

    /* Load */
    fun loadModule() {
        IOScope.launch {
            ModuleManager.modules.forEach { module ->
                val moduleFile = File("${Sakura.DIRECTORY}/modules/${module.name.key}.json")
                moduleFile.checkFile()
                val moduleJson = gsonPretty.fromJson(moduleFile.readText(), JsonObject::class.java)

                module.settings.forEach { setting ->
                    when (setting.key.key) {
                        "toggle" -> {
                            if (setting is BooleanSetting) {
                                if (moduleJson.get("toggle").asBoolean) module.enable()
                                else module.disable()
                            }
                            return@forEach
                        }

                        "key-bind" -> {
                            if (setting is KeyBindSetting) setting.value = KeyBind(KeyBind.Type.KEYBOARD, moduleJson.get("key-bind").asInt)
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
                        is KeyBindSetting -> setting.value = KeyBind(KeyBind.Type.KEYBOARD, moduleJson.get(setting.key.key).asInt)
                        is ColorSetting -> setting.value = ColorRGB(moduleJson.get(setting.key.key).asInt)
                        is EnumSetting<*> -> setting.setWithName(moduleJson.get(setting.key.key).asString)
                        else -> {}
                    }
                }
            }
        }
    }

    /* Save */
    fun saveModule() {
        val father = JsonObject()

        ModuleManager.modules.forEach { module ->

            val moduleFile = File("${Sakura.DIRECTORY}/modules/${module.name.key}.json")
            moduleFile.checkFile()
            val moduleJson = JsonObject()

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
            father.add(module.name.key, moduleJson)
        }
    }

}