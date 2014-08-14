package com.michael.e.liquislots.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

public class ItemLiquipackProtector extends Item implements ILiquipackProtection {

    private ISpecialArmor.ArmorProperties properties;

    public ItemLiquipackProtector(ISpecialArmor.ArmorProperties properties) {
        setUnlocalizedName("liquipackProtector");
        setCreativeTab(CreativeTabs.tabCombat);
        setMaxDamage((int) (properties.AbsorbMax * properties.AbsorbRatio));
        this.properties = properties;
    }

    @Override
    public boolean isDamageable() {
        return super.isDamageable();
    }

    @Override
    public ISpecialArmor.ArmorProperties getProtectionProps(ItemStack stack) {
        return properties;
    }
}
