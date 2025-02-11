package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.graphics.utils.RenderUtils3D.worldSpaceToScreenSpace
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.managers.impl.FriendManager.isFriend
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

            for (ent in world.players().filter { it.distanceSqTo(player) <= range.sq }) {

                if (ent == player && mc.options.cameraType.isFirstPerson) continue

                val x0 = (ent.xOld + (ent.x - ent.xOld) * e.tickDelta).toFloat()
                val y0 = (ent.yOld + (ent.y - ent.yOld) * e.tickDelta).toFloat() + 0.1f
                val z0 = (ent.zOld + (ent.z - ent.zOld) * e.tickDelta).toFloat()

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

                val x = position0.x
                val y = position0.y

                val name = "${ent.displayName?.string}"
                val health = ent.health.toInt()

                val nameWidth = FontRenderers.getStringWidth(name)
                val healthWidth = FontRenderers.getStringWidth(" $health")

                val width = nameWidth + healthWidth
                val startX = x - width / 2f

                val friend = isFriend(ent.displayName?.string ?: "")

                RenderUtils2D.drawRectFilled(startX - 2, y - 1, width + 4, FontRenderers.getHeight() + 2,
                    ColorRGB.BLACK.alpha(0.9f))

                //name
                FontRenderers.drawString(name, startX, y,
                    if (friend) ColorRGB.GREEN else ColorRGB.WHITE)

                //health
                FontRenderers.drawString(" $health", startX + nameWidth, y,
                    if (health >= 20) ColorRGB(0, 255, 0)
                    else if (health >= 10) ColorRGB(255, 255, 0)
                    else ColorRGB(255, 0, 0)
                )
            }
        }
    }

}