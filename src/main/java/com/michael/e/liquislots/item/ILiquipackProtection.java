package com.michael.e.liquislots.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

public interface ILiquipackProtection {

    public ISpecialArmor.ArmorProperties getProtectionProps(ItemStack stack);
}
