package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.impl.RegisterModuleEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.utils.clazz.ClassUtils.instance
import dev.exceptionteam.sakura.utils.clazz.classes
import java.lang.reflect.Modifier

object ModuleManager {
    lateinit var modules: Array<AbstractModule>

    init {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            if (event.isCancelled) return@nonNullListener

            // Only in the game
            if (mc.screen != null) return@nonNullListener

            modules.forEach {
                if (it.keyBind == event.keyBind && event.action == 1) it.toggle()
            }
        }

        loadModules()
    }

    private fun loadModules() {
        try {
            modules = emptyArray<AbstractModule>() // 防止 UninitializedPropertyAccessException

            classes.asSequence()
                .filter { Modifier.isFinal(it.modifiers) }
                .filter { it.name.startsWith("dev.exceptionteam.sakura.features.modules.impl") }
                .filter { AbstractModule::class.java.isAssignableFrom(it) }
                .map { it.instance as AbstractModule }
                .forEach {
                    modules += it
                }

            val event = RegisterModuleEvent(modules.toMutableList())
            event.post()
            modules = event.modules.sortedBy { it.name.key }.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}