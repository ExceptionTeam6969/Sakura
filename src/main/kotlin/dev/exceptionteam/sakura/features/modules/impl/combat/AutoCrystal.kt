package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.Render2DEvent
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.graphics.utils.RenderUtils3D.worldSpaceToScreenSpace
import dev.exceptionteam.sakura.managers.impl.HotbarManager.SwitchMode
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.combat.DamageCalculation
import dev.exceptionteam.sakura.utils.combat.PredictUtils.predictMotion
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
import dev.exceptionteam.sakura.utils.math.vector.Vec3f
import dev.exceptionteam.sakura.utils.player.InteractionUtils.attack
import dev.exceptionteam.sakura.utils.player.InteractionUtils.useItem
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.canPlaceCrystal
import dev.exceptionteam.sakura.utils.world.aroundBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.boss.enderdragon.EndCrystal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import org.joml.Vector4f

object AutoCrystal: Module(
    name = "auto-crystal",
    category = Category.COMBAT
) {

    private val page by setting("page", Page.GENERAL)

    // General
    private val targetRange by setting("target-range", 12f, 0f..24f, 0.5f) { page == Page.GENERAL }
    private val pauseWhileEating by setting("pause-while-eating", true) { page == Page.GENERAL }
    private val switchMode by setting("switch-mode", SwitchMode.SWITCH) { page == Page.GENERAL }
    private val rotation by setting("rotation", true) { page == Page.GENERAL }

    // Calculation
    private val predict by setting("predict", true) { page == Page.CALCULATION }
    private val predictTick by setting("predict-ticks", 4, 0..20) { page == Page.CALCULATION && predict }

    // Place
    private val placeDelay by setting("place-delay", 50, 0..1000, 5) { page == Page.PLACE }
    private val placeRange by setting("place-range", 4f, 0f..6f, 0.5f) { page == Page.PLACE }
    private val placeMaxSelfDmg by setting("place-max-self-dmg", 12.0, 0.0..20.0) { page == Page.PLACE }
    private val placeMinDmg by setting("place-min-dmg", 2.0, 0.0..20.0) { page == Page.PLACE }
    private val placeSwing by setting("place-swing", true) { page == Page.PLACE }
    private val priorityPlaceDiff by setting("priority-place-diff", 1.0f, 0.0f..10.0f) { page == Page.PLACE }

    // Break
    private val breakDelay by setting("break-delay", 50, 0..1000, 5) { page == Page.BREAK }
    private val breakRange by setting("break-range", 4f, 0f..6f, 0.5f) { page == Page.BREAK }
    private val breakMaxSelfDmg by setting("break-max-self-dmg", 12.0, 0.0..20.0) { page == Page.BREAK }
    private val breakMinDmg by setting("break-min-dmg", 2.0, 0.0..20.0) { page == Page.BREAK }
    private val breakSwing by setting("break-swing", true) { page == Page.BREAK }

    // Render
    private val color by setting("color", ColorRGB(255, 50, 50)) { page == Page.RENDER }
    private val showDmg by setting("show-dmg", true) { page == Page.RENDER }

    private val renderer = ESPRenderer().apply { aFilled = 60 }
    private val placeTimer = TimerUtils()
    private val breakTimer = TimerUtils()

    private var crystalInfo: CrystalInfo? = null

    override fun hudInfo(): String? {
        crystalInfo?.let {
            return String.format("%.1f/%.1f", it.selfDmg, it.targetDmg)
        }
        return null
    }

    init {
        onEnable {
            cleanup()
        }

        onDisable {
            cleanup()
        }

        nonNullListener<Render3DEvent> {
            crystalInfo?.let { inf ->
                renderer.add(inf.pos.below(), color)
                renderer.render(true)
            }
        }

        nonNullListener<Render2DEvent> {
            if (showDmg) {
                crystalInfo?.let { inf ->
                    val pos0 = inf.pos.below().center

                    val pos = worldSpaceToScreenSpace(Vec3f(pos0.x.toFloat(), pos0.y.toFloat(), pos0.z.toFloat()))

                    var position0: Vector4f? = null
                    if (pos.z in 0.0..1.0) {
                        position0 = Vector4f(pos.x, pos.y, pos.z, 0.0f)
                        position0.x = pos.x.coerceAtMost(position0.x)
                        position0.y = pos.y.coerceAtMost(position0.y)
                        position0.z = pos.x.coerceAtLeast(position0.z)
                    }

                    position0?.let {
                        val x = position0.x
                        val y = position0.y

                        val str = String.format("%.1f/%.1f", inf.selfDmg, inf.targetDmg)
                        val width = FontRenderers.getStringWidth(str) / 2f

                        FontRenderers.drawString(str, x - width, y, ColorRGB(255, 255, 255))
                    }
                }
            }
        }

        nonNullListener<TickEvents.Update> {
            val target = getTargetPlayer(targetRange) ?: run {
                cleanup()
                return@nonNullListener
            }

            if (pauseWhileEating && player.isUsingItem) {
                cleanup()
                return@nonNullListener
            }

            val breakInfo = if (breakTimer.passed(breakDelay)) getBreakCrystal(target) else null
            val placeInfo = if (placeTimer.passed(placeDelay)) getPlacePos(target) else null

            if (placeTimer.passed(placeDelay) || breakTimer.passed(breakDelay)) crystalInfo = null

            breakInfo?.let attack@ { breakInfo ->
                placeInfo?.let place@ { placeInfo ->
                    if (placeInfo.targetDmg < breakInfo.targetDmg) return@place
                    if (placeInfo.targetDmg - breakInfo.targetDmg > priorityPlaceDiff) {
                        placeCrystal(placeInfo)
                        return@attack
                    }
                }

                breakCrystal(breakInfo)
                return@nonNullListener
            }

            placeInfo?.let {
                placeCrystal(it)
            }

        }

    }

    private fun cleanup() {
        crystalInfo = null
        renderer.clear()

        placeTimer.reset()
        breakTimer.reset()
    }

    /**
     * Get the place position for the crystal.
     * @return The place position or null if there is no place position.
     */
    private fun NonNullContext.getPlacePos(target: Player): CrystalInfo? {
        val crystalInfo = mutableListOf<CrystalInfo>()

        val predictOffset = if (predict) predictMotion(target, predictTick) else Vec3.ZERO

        val predictPos = target.position().add(predictOffset)

        val selfCalc = DamageCalculation(this, player, player.position())
        val targetCalc = DamageCalculation(this, target, predictPos)

        target.blockPosition()
            .aroundBlock(6)
            .filter { it.center.distanceSqTo(player) <= placeRange.sq }
            .filter { canPlaceCrystal(it) }
            .forEach {
                val blockPos = it.above()
                val pos = blockPos.bottomCenter

                crystalInfo.add(
                    CrystalInfo(
                        it.above(),
                        selfCalc.calcDamage(pos.x, pos.y, pos.z, false,
                            BlockPos.MutableBlockPos()),    // Self damage
                        targetCalc.calcDamage(pos.x, pos.y, pos.z, predict,
                            BlockPos.MutableBlockPos()),    // Target damage
                        null    // No entity for breaking
                    )
                )
            }

        crystalInfo
            .filter { it.selfDmg <= it.targetDmg && it.selfDmg <= placeMaxSelfDmg && it.targetDmg >= placeMinDmg }
            .sortedBy { it }
            .let { return it.firstOrNull() }
    }

    private fun NonNullContext.getBreakCrystal(target: Player): CrystalInfo? {
        val crystalInfo = mutableListOf<CrystalInfo>()

        val predictOffset = if (predict) predictMotion(target, predictTick) else Vec3.ZERO

        val predictPos = target.position().add(predictOffset)

        val selfCalc = DamageCalculation(this, player, player.position())
        val targetCalc = DamageCalculation(this, target, predictPos)

        world.entitiesForRendering()
            .filterIsInstance<EndCrystal>()
            .filter { it.distanceSqTo(player) <= breakRange.sq }
            .forEach {
                crystalInfo.add(
                    CrystalInfo(
                        it.blockPosition(),
                        selfCalc.calcDamage(it.x, it.y, it.z, false, BlockPos.MutableBlockPos()),    // Self damage
                        targetCalc.calcDamage(it.x, it.y, it.z, predict, BlockPos.MutableBlockPos()),    // Target damage,
                        it
                    )
                )
            }

        crystalInfo
            .filter { it.selfDmg < it.targetDmg && it.selfDmg <= breakMaxSelfDmg && it.targetDmg >= breakMinDmg }
            .sortedBy { it }
            .let { return it.firstOrNull() }
    }

    private fun NonNullContext.breakCrystal(info: CrystalInfo) {
        if (info.entity == null) return

        breakTimer.reset()
        crystalInfo = info
        attack(info.entity, rotation, breakSwing)
    }

    private fun NonNullContext.placeCrystal(info: CrystalInfo) {
        placeTimer.reset()
        crystalInfo = info
        useItem(info.pos.below(), Items.END_CRYSTAL, switchMode, placeSwing, rotation)
    }

    private data class CrystalInfo(
        val pos: BlockPos,
        val selfDmg: Float,
        val targetDmg: Float,
        val entity: EndCrystal?       // Only used for breaking
    ): Comparable<CrystalInfo> {
        override fun compareTo(other: CrystalInfo): Int =
            if (targetDmg == other.targetDmg) {
                // Break crystals with lower self damage first
                selfDmg.compareTo(other.selfDmg)
            } else {
                // Break crystals with higher target damage first
                other.targetDmg.compareTo(targetDmg)
            }
    }

    private enum class Page(override val key: CharSequence): TranslationEnum {
        GENERAL("general"),
        CALCULATION("calculation"),
        PLACE("place"),
        BREAK("break"),
        RENDER("render")
    }
}