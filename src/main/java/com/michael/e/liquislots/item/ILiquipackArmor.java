package com.michael.e.liquislots.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

public interface ILiquipackArmor {

    public ISpecialArmor.ArmorProperties getProtectionProps(ItemStack stack);
}
