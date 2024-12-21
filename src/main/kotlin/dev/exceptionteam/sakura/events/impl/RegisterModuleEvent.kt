package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import dev.exceptionteam.sakura.features.modules.AbstractModule

class RegisterModuleEvent(
    val modules: MutableList<AbstractModule>
): Event()