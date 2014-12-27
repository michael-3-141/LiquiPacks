package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.common.upgrade.LiquidXPUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLiquidXPConfig extends Container {

    private final LiquidXPUpgrade upgrade;
    private ItemStack liquipack;
    private LiquipackStack liquipackStack;
    private int upgradeIndex;

    public ContainerLiquidXPConfig(EntityPlayer player, ItemStack liquipack, int upgradeIndex) {
        this.liquipack = liquipack;
        this.liquipackStack = new LiquipackStack(liquipack);
        this.upgrade = (LiquidXPUpgrade) liquipackStack.getUpgrade(upgradeIndex);
        this.upgradeIndex = upgradeIndex;
        InventoryPlayer invPlayer = player.inventory;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, i * 18 + 107));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 165));
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
