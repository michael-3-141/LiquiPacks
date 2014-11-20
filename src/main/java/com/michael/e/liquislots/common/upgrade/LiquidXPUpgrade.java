package com.michael.e.liquislots.common.upgrade;

import com.michael.e.liquislots.common.util.LiquipackUpgrade;
import net.minecraft.nbt.NBTTagCompound;

public class LiquidXPUpgrade extends LiquipackUpgrade {

    public static final String ID = "LiquidXP";

    public static final int MODE_DISABLED = 0;
    public static final int MODE_DRAIN_XP = 0;
    public static final int MODE_DRAIN_TANK = 0;

    public LiquidXPUpgrade() {
        super(ID, null);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("tank", 1);
        compound.setInteger("mode", 0);
        setInfo(compound);
    }

    public void setTank(int tank){
        getInfo().setInteger("tank", tank);
    }

    public int getTank(){
        return getInfo().getInteger("tank");
    }

    public void setMode(int mode){
        getInfo().setInteger("mode", mode);
    }

    public int getMode(){
        return getInfo().getInteger("mode");
    }

    public static boolean isLiquidXPUpgrade(LiquipackUpgrade upgrade) {
        return upgrade.getUpgradeName().equalsIgnoreCase("LiquidXP");
    }
}
