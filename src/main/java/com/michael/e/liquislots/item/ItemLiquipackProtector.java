package com.michael.e.liquislots.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

public class ItemLiquipackProtector extends ItemLiquipacksBase implements ILiquipackArmor {

    private double absorbAmount;

    public ItemLiquipackProtector(double absorbAmount, int maxDamage) {
        super();
        setMaxDamage(maxDamage);
        this.absorbAmount = absorbAmount;
    }

    @Override
    public boolean isDamageable() {
        return super.isDamageable();
    }

    @Override
    public ISpecialArmor.ArmorProperties getProtectionProps(ItemStack stack) {
        return new ISpecialArmor.ArmorProperties(0, absorbAmount, getMaxDamage() + 1 - stack.getItemDamage());
    }
}
