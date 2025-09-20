package team.exception.sakura.events

import net.neoforged.bus.api.Event
import team.exception.sakura.modules.AbstractModule

class ToggleModuleEvent(
    val module: AbstractModule,
    val enabled: Boolean
): Event()