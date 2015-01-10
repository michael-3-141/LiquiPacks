package com.michael.e.liquislots.item;

import com.michael.e.liquislots.common.upgrade.LiquipackUpgrade;
import net.minecraft.item.ItemStack;

public interface ILiquipackUpgrade {

    public LiquipackUpgrade getUpgradeForStack(ItemStack stack);
}
