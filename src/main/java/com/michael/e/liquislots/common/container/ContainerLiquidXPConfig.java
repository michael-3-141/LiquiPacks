package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.common.upgrade.LiquidXPUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

public class ContainerLiquidXPConfig extends Container {

    private LiquidXPUpgrade upgrade = null;
    private ItemStack liquipack;
    private LiquipackStack liquipackStack;
    private int upgradeIndex;

    public ContainerLiquidXPConfig(EntityPlayer player, int upgradeIndex) {
        this.liquipack = player.inventory.armorItemInSlot(2);
        this.liquipackStack = new LiquipackStack(liquipack);
        this.upgradeIndex = upgradeIndex;
        try {
            this.upgrade = LiquidXPUpgrade.fromLiquipackUpgrade(liquipackStack.getUpgrade(upgradeIndex));
        }catch (ClassCastException e){
            player.closeScreen();
            return;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private int prevTank;
    private boolean prevDrainMode;

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        crafters.add(player);
        player.sendProgressBarUpdate(this, 0, upgrade.getTank());
        player.sendProgressBarUpdate(this, 1, upgrade.getMode());
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id){
            case 0:
                upgrade.setTank(data);
                break;
            case 1:
                upgrade.setMode(data);
                break;
        }

        liquipackStack.setUpgrade(upgrade, upgradeIndex);
    }
}
