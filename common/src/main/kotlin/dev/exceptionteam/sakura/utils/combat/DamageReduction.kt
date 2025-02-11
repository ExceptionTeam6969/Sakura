/*
 * Copyright (c) 2021-2022, SagiriXiguajerry. All rights reserved.
 * This repository will be transformed to SuperMic_233.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package dev.exceptionteam.sakura.utils.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.features.modules.impl.client.CombatSettings
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import dev.exceptionteam.sakura.utils.world.getEnchantmentLevel
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.enchantment.Enchantments
import kotlin.let
import kotlin.math.max
import kotlin.math.min
import kotlin.run

class DamageReduction(val entity: LivingEntity, val updateDelay: Long = 100) {
    private val armorValue = entity.armorValue.toFloat()
    private val toughness = entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()
    private var resistanceMultiplier: Float
    private var genericMultiplier: Float
    private var blastMultiplier: Float
    private val updateTimer = TimerUtils()

    init {
        var genericEPF = 0
        var blastEPF = 0

        for (itemStack in entity.armorSlots) {
            genericEPF += itemStack.getEnchantmentLevel(Enchantments.PROTECTION)
            blastEPF += itemStack.getEnchantmentLevel(Enchantments.BLAST_PROTECTION) * 2
        }

        resistanceMultiplier = entity.getEffect(MobEffects.DAMAGE_RESISTANCE)?.let {
            max(1.0f - (it.amplifier + 1) * 0.2f, 0.0f)
        } ?: run {
            if (CombatSettings.assumeResistance) {
                0.8f
            } else {
                1.0f
            }
        }

        genericMultiplier = (1.0f - min(genericEPF, 20) / 25.0f)
        blastMultiplier = (1.0f - min(genericEPF + blastEPF, 20) / 25.0f)
    }

    fun calcDamage(damage: Float, isExplosion: Boolean): Float {
        if (updateTimer.passedAndReset(updateDelay.toInt())) update()
        return CombatRules.getDamageAfterAbsorb(damage, armorValue, toughness) *
                resistanceMultiplier *
                if (isExplosion) blastMultiplier
                else genericMultiplier
    }

    private fun NonNullContext.getDamageMultiplied(damage: Float): Float {
        val diff = world.difficulty.id
        return damage * if (diff == 0) 0f else if (diff == 2) 1f else if (diff == 1) 0.5f else 1.5f
    }

    private fun update() {
        var genericEPF = 0
        var blastEPF = 0

        for (itemStack in entity.armorSlots) {
            genericEPF += itemStack.getEnchantmentLevel(Enchantments.PROTECTION)
            blastEPF += itemStack.getEnchantmentLevel(Enchantments.BLAST_PROTECTION) * 2
        }

        resistanceMultiplier = entity.getEffect(MobEffects.DAMAGE_RESISTANCE)?.let {
            max(1.0f - (it.amplifier + 1) * 0.2f, 0.0f)
        } ?: run {
            if (CombatSettings.assumeResistance) {
                0.8f
            } else {
                1.0f
            }
        }

        genericMultiplier = (1.0f - min(genericEPF, 20) / 25.0f)
        blastMultiplier = (1.0f - min(genericEPF + blastEPF, 20) / 25.0f)
    }
}