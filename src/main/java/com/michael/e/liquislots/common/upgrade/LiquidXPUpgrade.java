package com.michael.e.liquislots.common.upgrade;

import net.minecraft.nbt.NBTTagCompound;

public class LiquidXPUpgrade extends LiquipackUpgrade {

    public static final int MODE_DISABLED = 0;
    public static final int MODE_DRAIN_XP = 1;
    public static final int MODE_DRAIN_TANK = 2;

    public LiquidXPUpgrade(){
        this(new NBTTagCompound());
    }

    public LiquidXPUpgrade(NBTTagCompound info){
        super(info, LiquipackUpgradeType.LIQUID_XP);
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
        return upgrade != null && upgrade.getType() == LiquipackUpgradeType.LIQUID_XP;
    }

    public static LiquidXPUpgrade fromLiquipackUpgrade(LiquipackUpgrade upgrade){
        return upgrade.getType() == LiquipackUpgradeType.LIQUID_XP ? new LiquidXPUpgrade(upgrade.getInfo()) : null;
    }
}
