package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.TankStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ContainerPlayerTanks extends Container implements OnInventoryChangedListener{

    private InventoryTankInterface tankInterface = new InventoryTankInterface(this);
    private EntityPlayer player;
    private TankStack tanks;

    public int selectedTank = 0;

    public ContainerPlayerTanks(EntityPlayer player){
        this.player = player;
        InventoryPlayer invPlayer = player.inventory;
        tanks = new TankStack(player.inventory.armorItemInSlot(2));

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

        addSlotToContainer(new Slot(tankInterface, 0, 61, 80));
        addSlotToContainer(new Slot(tankInterface, 1, 106, 80));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCraftingToCrafters(ICrafting player) {
        crafters.add(player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public void updateProgressBar(int id, int data) {
        /*if(id == 0){
            tanks.getTankForStack(0).getFluid().fluidID = data;
        }
        else{
            tanks.getTankForStack(0).getFluid().amount = data;
        }*/
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if(!player.worldObj.isRemote) {
            for (int i = 0; i < tankInterface.getSizeInventory(); i++) {
                if (tankInterface.getStackInSlot(i) != null) {
                    EntityItem item = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, tankInterface.getStackInSlot(i));
                    player.worldObj.spawnEntityInWorld(item);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        return null;
    }

    @Override
    public void onInventoryChanged() {
        ItemStack input = tankInterface.getStackInSlot(0);
        ItemStack result = tankInterface.getStackInSlot(1);
        FluidTank tank = tanks.getTankForStack(selectedTank);
        boolean success = false;
        if(input == null || result != null)return;
        if(FluidContainerRegistry.isFilledContainer(input)){
            if(tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), true);
                result = new ItemStack(input.getItem().getContainerItem(), 1);
                success = true;
            }
        }
        else if(FluidContainerRegistry.isEmptyContainer(input))
        {
            if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME){
                result = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
                tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                success = true;
            }

        }
        if(success) {
            tankInterface.setInventorySlotContents(1, result);
            tankInterface.decrStackSize(0, 1);
            tanks.setTankInStack(tank, selectedTank);
        }
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
}
