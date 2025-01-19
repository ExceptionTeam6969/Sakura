package dev.exceptionteam.sakura.graphics.general

import dev.exceptionteam.sakura.graphics.GlHelper
import dev.exceptionteam.sakura.graphics.utils.RenderUtils3D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB

class ESPRenderer {
    private var toRender0: MutableList<Info> = ArrayList()

    val toRender: List<Info>
        get() = toRender0

    var aFilled = 0
    var through = true

    val size: Int
        get() = toRender0.size
    fun add(pos: BlockPos, color: ColorRGB) {
        add(pos, color, DirectionMask.ALL)
    }

    fun add(pos: BlockPos, color: ColorRGB, sides: Int) {
        add(AABB(pos), color, sides)
    }

    fun add(box: AABB, color: ColorRGB) {
        add(box, color, DirectionMask.ALL)
    }

    fun add(box: AABB, color: ColorRGB, sides: Int) {
        add(Info(box, color, sides))
    }

    fun add(info: Info) {
        toRender0.add(info)
    }

    fun replaceAll(list: MutableList<Info>) {
        toRender0 = list
    }

    fun clear() {
        toRender0.clear()
    }

    fun render(clear: Boolean) {
        val filled = aFilled != 0
        if (toRender0.isEmpty() || (!filled)) return

        if (through) GlHelper.depth = false

        if (filled) {
            GlHelper.cull = false
            for ((box, color) in toRender0) {
                val a = (aFilled * (color.a / 255.0f)).toInt()
                RenderUtils3D.drawFilledBox(box, color.alpha(a))
            }
        }

        if (clear) clear()
        GlHelper.depth = true
    }

    data class Info(val box: AABB, val color: ColorRGB, val sides: Int) {
        constructor(box: AABB) : this(box, ColorRGB(255, 255, 255), DirectionMask.ALL)
        constructor(box: AABB, color: ColorRGB) : this(box, color, DirectionMask.ALL)
        constructor(pos: BlockPos) : this(AABB(pos), ColorRGB(255, 255, 255), DirectionMask.ALL)
        constructor(pos: BlockPos, color: ColorRGB) : this(AABB(pos), color, DirectionMask.ALL)
        constructor(pos: BlockPos, color: ColorRGB, sides: Int) : this(AABB(pos), color, sides)
    }
}