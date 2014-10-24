package com.michael.e.liquislots.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LiquipackUpgrade {

    private String upgradeName;
    private NBTTagCompound info;

    public LiquipackUpgrade(String upgradeName, NBTTagCompound info) {
        this.upgradeName = upgradeName;
        this.info = info;
    }

    public String getUpgradeName() {
        return upgradeName;
    }

    public void setUpgradeName(String upgradeName) {
        this.upgradeName = upgradeName;
    }

    public NBTTagCompound getInfo() {
        return info;
    }

    public void setInfo(NBTTagCompound info) {
        this.info = info;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        compound.setString("upgradeName", upgradeName);
        compound.setTag("info", info);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        if (compound == null) return;
        upgradeName = compound.getString("upgradeName");
        info = compound.getCompoundTag("info");
    }

    public static LiquipackUpgrade loadFromNBT(NBTTagCompound compound){
        return compound == null ? null : new LiquipackUpgrade(compound.getString("upgradeName"), compound.getCompoundTag("info"));
    }
}
