package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.Render3DEvent
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.general.ESPRenderer
import dev.exceptionteam.sakura.managers.impl.HotbarManager.SwitchMode
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.combat.DamageCalculator.crystalDamage
import dev.exceptionteam.sakura.utils.combat.PredictUtils.predictMotion
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.sq
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

    // Break
    private val breakDelay by setting("break-delay", 50, 0..1000, 5) { page == Page.BREAK }
    private val breakRange by setting("break-range", 4f, 0f..6f, 0.5f) { page == Page.BREAK }
    private val breakMaxSelfDmg by setting("break-max-self-dmg", 12.0, 0.0..20.0) { page == Page.BREAK }
    private val breakMinDmg by setting("break-min-dmg", 2.0, 0.0..20.0) { page == Page.BREAK }
    private val breakSwing by setting("break-swing", true) { page == Page.BREAK }

    // Render
    private val color by setting("color", ColorRGB(255, 50, 50)) { page == Page.RENDER }

    private val renderer = ESPRenderer().apply { aFilled = 60 }
    private val placeTimer = TimerUtils()
    private val breakTimer = TimerUtils()

    private var crystalInfo: CrystalInfo? = null

    init {
        nonNullListener<Render3DEvent> {
            crystalInfo?.let {
                renderer.add(it.pos.below(), color)
                renderer.render(true)
            }
        }

        nonNullListener<TickEvent.Update> {
            val target = getTargetPlayer(targetRange) ?: return@nonNullListener

            if (pauseWhileEating && player.isUsingItem) return@nonNullListener

            val breakInfo = getBreakCrystal(target)
            val placeInfo = getPlacePos(target)

            crystalInfo = null

            if (breakInfo != null && placeInfo != null) {
                if (breakInfo.targetDmg > placeInfo.targetDmg) {
                    crystalInfo = breakInfo
                    if (breakTimer.passedAndReset(breakDelay)) breakCrystal(breakInfo)
                } else if (breakInfo.targetDmg == placeInfo.targetDmg) {
                    if (breakInfo.selfDmg <= placeInfo.selfDmg) {
                        crystalInfo = breakInfo
                        if (breakTimer.passedAndReset(breakDelay)) breakCrystal(breakInfo)
                    } else {
                        crystalInfo = placeInfo
                        if (placeTimer.passedAndReset(placeDelay)) placeCrystal(placeInfo)
                    }
                } else {
                    crystalInfo = placeInfo
                    if (placeTimer.passedAndReset(placeDelay)) placeCrystal(placeInfo)
                }
            } else if (breakInfo != null) {
                crystalInfo = breakInfo
                if (breakTimer.passedAndReset(breakDelay)) breakCrystal(breakInfo)
            } else if (placeInfo != null) {
                crystalInfo = placeInfo
                if (placeTimer.passedAndReset(placeDelay)) placeCrystal(placeInfo)
            }

        }

    }

    /**
     * Get the place position for the crystal.
     * @return The place position or null if there is no place position.
     */
    private fun NonNullContext.getPlacePos(target: Player): CrystalInfo? {
        val crystalInfo = mutableListOf<CrystalInfo>()

        val predictOffset = if (predict) predictMotion(target, predictTick) else Vec3.ZERO

        val predictPos = target.position().add(predictOffset)
        val predictAABB = target.boundingBox.move(predictOffset)

        target.blockPosition()
            .aroundBlock(6)
            .filter { it.bottomCenter.distanceSqTo(player) <= placeRange.sq }
            .filter { canPlaceCrystal(it) }
            .forEach {
                crystalInfo.add(
                    CrystalInfo(
                        it.above(),
                        crystalDamage(player, player.position(), player.boundingBox, it.above().bottomCenter),    // Self damage
                        crystalDamage(target, predictPos, predictAABB, it.above().bottomCenter),    // Target damage
                        null    // No entity for breaking
                    )
                )
            }

        crystalInfo
            .filter { it.selfDmg <= it.targetDmg && it.selfDmg <= placeMaxSelfDmg && it.targetDmg >= placeMinDmg }
            .sortedBy { it }
            .let { return it.firstOrNull() }

        return null
    }

    private fun NonNullContext.getBreakCrystal(target: Player): CrystalInfo? {
        val crystalInfo = mutableListOf<CrystalInfo>()

        val predictOffset = if (predict) predictMotion(target, predictTick) else Vec3.ZERO

        val predictPos = target.position().add(predictOffset)
        val predictAABB = target.boundingBox.move(predictOffset)

        world.entitiesForRendering()
            .filterIsInstance<EndCrystal>()
            .filter { it.distanceSqTo(player) <= breakRange.sq }
            .forEach {
                crystalInfo.add(
                    CrystalInfo(
                        it.blockPosition(),
                        crystalDamage(player, player.position(), player.boundingBox, it.position()),    // Self damage
                        crystalDamage(target, predictPos, predictAABB, it.position()),    // Target damage,
                        it
                    )
                )
            }

        crystalInfo
            .filter { it.selfDmg < it.targetDmg && it.selfDmg <= breakMaxSelfDmg && it.targetDmg >= breakMinDmg }
            .sortedBy { it }
            .let { return it.firstOrNull() }

        return null
    }

    private fun NonNullContext.breakCrystal(info: CrystalInfo) {
        if (info.entity == null) return

        attack(info.entity, rotation, breakSwing)
    }

    private fun NonNullContext.placeCrystal(info: CrystalInfo) {
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