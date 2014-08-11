package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.item.ItemLiquipackBucket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLiquipackBucketOptions extends Container {

    private ItemStack bucket;
    
    public ContainerLiquipackBucketOptions(EntityPlayer player, ItemStack bucket) {
        this.bucket = bucket;
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
        player.sendProgressBarUpdate(this, 0, ItemLiquipackBucket.getSelectedTank(bucket));
        player.sendProgressBarUpdate(this, 1, ItemLiquipackBucket.isDrainingMode(bucket) ? 1 : 0);
    }

    @Override
    public void detectAndSendChanges() {
        /*for(ICrafting player : ((List<ICrafting>)crafters)){
            if(ItemLiquipackBucket.getTank(bucket) != ItemLiquipackBucket.getTank(bucket)) {
                player.sendProgressBarUpdate(this, 0, ItemLiquipackBucket.getTank(bucket));
            }
            if (ItemLiquipackBucket.isDrainingMode(bucket) != prevDrainMode){
                player.sendProgressBarUpdate(this, 1, ItemLiquipackBucket.isDrainingMode(bucket) ? 1 : 0);
            }
            prevTank = ItemLiquipackBucket.getTank(bucket);
            prevDrainMode = ItemLiquipackBucket.isDrainingMode(bucket);
        }*/
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id){
            case 0:
                ItemLiquipackBucket.setSelectedTank(bucket, data);
                break;
            case 1:
                ItemLiquipackBucket.setDrainingMode(bucket, data == 1);
        }
    }
}
