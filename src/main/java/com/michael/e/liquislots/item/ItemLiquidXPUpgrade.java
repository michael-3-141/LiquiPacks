package com.michael.e.liquislots.item;

import com.michael.e.liquislots.common.util.LiquipackUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemLiquidXPUpgrade extends ItemLiquipacksBase implements ILiquipackUpgrade {

    public static final LiquipackUpgrade UPGRADE = new LiquipackUpgrade("LiquidXP", new NBTTagCompound());

    static {

    }

    public ItemLiquidXPUpgrade() {
        super();
    }

    @Override
    public LiquipackUpgrade getUpgradeForStack(ItemStack stack) {
        return UPGRADE;
    }
}
