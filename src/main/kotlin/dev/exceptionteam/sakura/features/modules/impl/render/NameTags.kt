package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.RenderUtils3D.worldSpaceToScreenSpace
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
import dev.exceptionteam.sakura.utils.math.vector.Vec3f
import org.joml.Vector4f

object NameTags: Module(
    name = "name-tags",
    category = Category.RENDER,
) {

    private val range by setting("range", 128, 0..512)

    init {

        nonNullListener<Render2DEvent> { e ->

            for (ent in world.players.filter { it.distanceSqTo(player) <= range.sq }) {

                if (ent == player && mc.options.perspective.isFirstPerson) continue

                val x0 = (ent.prevX + (ent.x - ent.prevX) * e.tickDelta).toFloat()
                val y0 = (ent.prevY + (ent.y - ent.prevY) * e.tickDelta).toFloat() + 0.1f
                val z0 = (ent.prevZ + (ent.z - ent.prevZ) * e.tickDelta).toFloat()

                var vector = Vec3f(x0, y0 + 2f, z0)
                var position0: Vector4f? = null
                vector = worldSpaceToScreenSpace(Vec3f(vector.x, vector.y, vector.z))
                if (vector.z > 0 && vector.z < 1) {
                    position0 = Vector4f(vector.x, vector.y, vector.z, 0.0f)
                    position0.x = vector.x.coerceAtMost(position0.x)
                    position0.y = vector.y.coerceAtMost(position0.y)
                    position0.z = vector.x.coerceAtLeast(position0.z)
                }

                if (position0 == null) continue

                val x = position0.x.toFloat()
                val y = position0.y.toFloat()

                val str = "${ent.displayName?.string}"

                val stringWidth = FontRenderers.getStringWidth(str)

                RenderUtils2D.drawRectFilled(x - 1 - stringWidth / 2f, y - 1, stringWidth + 2, FontRenderers.getHeight(),
                    ColorRGB.BLACK.alpha(0.9f))

                FontRenderers.drawString(ent.displayName!!.string, x - stringWidth / 2f, y, ColorRGB.WHITE)

            }

        }

    }

}