package com.michael.e.liquislots.common.upgrade;

import net.minecraft.nbt.NBTTagCompound;

public class LiquipackUpgrade {

    private NBTTagCompound info;
    private LiquipackUpgradeType type;

    public LiquipackUpgrade(NBTTagCompound compound){
        readFromNBT(compound);
    }

    public LiquipackUpgrade(NBTTagCompound info, LiquipackUpgradeType type) {
        this.info = info;
        this.type = type;
    }

    public NBTTagCompound getInfo() {
        return info;
    }

    public void setInfo(NBTTagCompound info) {
        this.info = info;
    }

    public LiquipackUpgradeType getType() {
        return type;
    }

    public void setType(LiquipackUpgradeType type) {
        this.type = type;
    }

    public void readFromNBT(NBTTagCompound compound){
        info = compound.getCompoundTag("info");
        type = LiquipackUpgradeType.loadFromNBT(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        compound.setTag("info", info);
        type.writeToNBT(compound);
        return compound;
    }
}