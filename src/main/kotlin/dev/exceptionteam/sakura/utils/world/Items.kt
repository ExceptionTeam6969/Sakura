package dev.exceptionteam.sakura.utils.world

import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper

fun ItemStack.getEnchantmentLevel(enchantment: ResourceKey<Enchantment>) =
    EnchantmentHelper.getItemEnchantmentLevel(enchantment.entry, this)

val ResourceKey<Enchantment>.entry get() =
    Minecraft.getInstance().level!!.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(this).get()