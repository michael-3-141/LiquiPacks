package com.michael.e.liquislots.block;

import com.michael.e.liquislots.item.ItemLiquipack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLiquipackWorkbench extends TileEntity implements IInventory{

    private ItemStack stack;

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound compound = new NBTTagCompound();
        if(stack != null) {
            stack.writeToNBT(compound);
            nbt.setTag("liquipack", compound);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if(nbt.hasKey("liquipack")){
            stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("liquipack"));
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemstack = getStackInSlot(i);

        if(itemstack != null)
        {
            if(itemstack.stackSize <= j)
            {
                setInventorySlotContents(i, null);
            }else{
                itemstack = itemstack.splitStack(j);
                markDirty();
            }
        }

        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public String getInventoryName() {
        return "lpWorkbench";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord, yCoord, zCoord) < 60;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        return stack.getItem() instanceof ItemLiquipack;
    }
}
