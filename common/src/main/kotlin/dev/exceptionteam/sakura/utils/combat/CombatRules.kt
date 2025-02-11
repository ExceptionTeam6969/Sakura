package dev.exceptionteam.sakura.utils.combat

import net.minecraft.util.Mth

object CombatRules {
    fun getDamageAfterAbsorb(damage: Float, totalArmor: Float, toughnessAttribute: Float): Float {
        val f = 2.0f + toughnessAttribute / 4.0f
        val f1 = Mth.clamp(totalArmor - damage / f, totalArmor * 0.2f, 20.0f)
        return damage * (1.0f - f1 / 25.0f)
    }
}