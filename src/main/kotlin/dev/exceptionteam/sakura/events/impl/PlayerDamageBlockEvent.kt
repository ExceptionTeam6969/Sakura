package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.Event
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction


class PlayerDamageBlockEvent internal constructor(
    var pos: BlockPos? = null,
    var facing: Direction? = null
) : Event()

