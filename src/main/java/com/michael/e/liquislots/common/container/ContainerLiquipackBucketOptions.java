package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.item.ItemHandPump;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

public class ContainerLiquipackBucketOptions extends Container {

    private ItemStack bucket;
    
    public ContainerLiquipackBucketOptions(ItemStack bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private int prevTank;
    private int prevMode;

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        crafters.add(player);
        player.sendProgressBarUpdate(this, 0, ItemHandPump.getSelectedTank(bucket));
        player.sendProgressBarUpdate(this, 1, ItemHandPump.getMode(bucket));
    }

    @Override
    public void detectAndSendChanges() {
        /*for(ICrafting player : ((List<ICrafting>)crafters)){
            if(ItemHandPump.getSelectedTank(bucket) != ItemHandPump.getSelectedTank(bucket)) {
                player.sendProgressBarUpdate(this, 0, ItemHandPump.getSelectedTank(bucket));
            }
            if (ItemHandPump.getMode(bucket) != prevDrainMode){
                player.sendProgressBarUpdate(this, 1, ItemHandPump.getMode(bucket) ? 1 : 0);
            }
            prevTank = ItemHandPump.getSelectedTank(bucket);
            prevDrainMode = ItemHandPump.getMode(bucket);
        }*/
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id){
            case 0:
                ItemHandPump.setSelectedTank(bucket, data);
                break;
            case 1:
                ItemHandPump.setMode(bucket, data);
        }
    }
}
