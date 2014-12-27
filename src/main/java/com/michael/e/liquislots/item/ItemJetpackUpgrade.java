package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.util.LiquipackUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemJetpackUpgrade extends ItemLiquipacksBase implements ILiquipackUpgrade{

    private static LiquipackUpgrade upgrade = new LiquipackUpgrade("jetpack", new NBTTagCompound());

    public ItemJetpackUpgrade() {
        super();
        setUnlocalizedName("jetpackUpgrade");
        setTextureName(Reference.MOD_ID + ":lpJetpackUpgrade");
    }


    @Override
    public LiquipackUpgrade getUpgradeForStack(ItemStack stack) {
        return upgrade;
    }
}
