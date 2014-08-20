package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.common.SFluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContainerPlayerTanks extends Container{

    //private InventoryTankInterface tankInterface = new InventoryTankInterface(this);
    private EntityPlayer player;
    private LiquipackStack tanks;

    public int selectedTank = 0;

    public ContainerPlayerTanks(EntityPlayer player){
        this.player = player;
        InventoryPlayer invPlayer = player.inventory;
        tanks = new LiquipackStack(player.inventory.armorItemInSlot(2));

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 165));
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, i * 18 + 107));
            }
        }



        addSlotToContainer(new BucketSlot(tanks, 0, 61, 80));
        addSlotToContainer(new BucketResultSlot(tanks, 1, 106, 80));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private void sendTanksToCrafter(ICrafting player){
        int i = 0;
        for(SFluidTank fluidTank : tanks.getTanks()){
            player.sendProgressBarUpdate(this, i, fluidTank.getFluid() != null && fluidTank.getFluid().getFluid() != null? fluidTank.getFluid().fluidID : -1);
            player.sendProgressBarUpdate(this, i + 1, fluidTank.getFluid() != null && fluidTank.getFluid().amount > 0 ? fluidTank.getFluidAmount() : -1);
            player.sendProgressBarUpdate(this, i + 2, fluidTank.getCapacity());
            i++;
        }
        player.sendProgressBarUpdate(this, 500, 500);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCraftingToCrafters(ICrafting player) {
        //super.addCraftingToCrafters(player);
        //sendTanksToCrafter(player);
    }

    private SFluidTank[] prevTanks;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if(!Arrays.equals(prevTanks, tanks.getTanks())) {
            for (Object crafting : crafters) {
                sendTanksToCrafter((ICrafting) crafting);
            }
        }
        prevTanks = tanks.getTanks().clone();
    }

    @SideOnly(Side.CLIENT)
    private List<Integer> recievedTanks = new ArrayList<Integer>();

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        if(id == 500 && data == 500) {
            NBTTagList list = new NBTTagList();
            for (int i = 0; i < recievedTanks.size() / 3; i++) {
                SFluidTank tank = new SFluidTank(recievedTanks.get(i * 3) == -1 ? null : new FluidStack(recievedTanks.get(i * 3), recievedTanks.get((i * 3) + 1)), recievedTanks.get((i * 3) + 2));
                list.appendTag(tank.writeToNBT(new NBTTagCompound()));
            }
            tanks.setTanks(list);
            recievedTanks.clear();
        }
        else{
            recievedTanks.add(data);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if(!player.worldObj.isRemote) {
            for (int i = 0; i < tanks.getSizeInventory(); i++) {
                if (tanks.getStackInSlot(i) != null) {
                    EntityItem item = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, tanks.getStackInSlot(i));
                    player.worldObj.spawnEntityInWorld(item);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();

            if (i >= 36) {
                if (!mergeItemStack(stack, 0, 36, false)) {
                    return null;
                }
            }else if(!FluidContainerRegistry.isContainer(stack) || !mergeItemStack(stack, 36, 37, false)) {
                return null;
            }

            if (stack.stackSize == 0) {
                slot.putStack(null);
            }else{
                slot.onSlotChanged();
            }

            slot.onPickupFromSlot(player, stack);

            return result;
        }

        return null;
    }

    private class InventoryTankInterface implements IInventory{

        ItemStack[] stacks = new ItemStack[2];
        OnInventoryChangedListener listener;

        public InventoryTankInterface(OnInventoryChangedListener listener)
        {
            this.listener = listener;
        }

        @Override
        public int getSizeInventory() {
            return stacks.length;
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            return stacks[i];
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
        public ItemStack getStackInSlotOnClosing(int var1) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int i, ItemStack stack) {
            stacks[i] = stack;

            if(stack != null && stack.stackSize > getInventoryStackLimit())
            {
                stack.stackSize = getInventoryStackLimit();
            }

            markDirty();
        }

        @Override
        public String getInventoryName() {
            return "tankIO";
        }

        @Override
        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public void markDirty() {
            if(player.worldObj.isRemote)return;
            listener.onInventoryChanged();
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return true;
        }

        @Override
        public void openInventory() {
        }

        @Override
        public void closeInventory() {
        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack stack) {
            return true;
        }

        public boolean incrStackSize(int i, int j) {
            ItemStack itemstack = getStackInSlot(i);

            if(itemstack != null) {
                if (itemstack.stackSize >= itemstack.getMaxStackSize()) {
                    itemstack.stackSize = itemstack.getMaxStackSize();
                    setInventorySlotContents(i, itemstack);
                } else {
                    itemstack.stackSize += j;
                    setInventorySlotContents(i, itemstack);
                    markDirty();
                    return true;
                }
            }
            return false;
        }
    }

    public static class BucketSlot extends Slot{

        public BucketSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return FluidContainerRegistry.isContainer(stack);
        }
    }

    public static class BucketResultSlot extends Slot{

        public BucketResultSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
