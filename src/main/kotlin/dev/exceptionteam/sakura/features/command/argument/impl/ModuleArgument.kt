package dev.exceptionteam.sakura.features.command.argument.impl

import dev.exceptionteam.sakura.features.command.argument.Argument
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.managers.impl.ModuleManager

class ModuleArgument(index: Int) : Argument<AbstractModule>(index) {
    override fun complete(input: String): List<String> {
        return ModuleManager.modules
            .map { it.name.key }
            .filter { it.startsWith(input, true) }
    }

    override fun convertToType(input: String): AbstractModule? {
        return ModuleManager.modules
            .firstOrNull { it.name.key.equals(input, true) }
    }

    override fun toString(): String {
        return "[Module]"
    }
}