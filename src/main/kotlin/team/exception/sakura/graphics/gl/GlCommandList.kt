package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.buffer.G2CommandList

class GlCommandList: G2CommandList() {

    private val commands: MutableList<GlCommand> = mutableListOf()

    internal fun add(command: () -> Unit) {
        commands.add(GlCommand(command))
    }

    override fun summit() {
        commands.forEach { it.func() }
    }

    override fun clear() {
        commands.clear()
    }

    override fun summitAndClear() {
        summit()
        clear()
    }

    class GlCommand(val func: () -> Unit)

}