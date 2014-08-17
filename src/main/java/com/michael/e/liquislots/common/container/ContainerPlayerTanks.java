package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.common.SFluidTank;
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

public class ContainerPlayerTanks extends Container implements OnInventoryChangedListener{

    private InventoryTankInterface tankInterface = new InventoryTankInterface(this);
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



        addSlotToContainer(new BucketSlot(tankInterface, 0, 61, 80));
        addSlotToContainer(new BucketResultSlot(tankInterface, 1, 106, 80));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private void sendTanksToCrafter(ICrafting player){
        /*int i = 0;
        for(SFluidTank fluidTank : tanks.getTanks()){
            player.sendProgressBarUpdate(this, i, fluidTank.getFluid() != null ? fluidTank.getFluid().fluidID : -1);
            player.sendProgressBarUpdate(this, i + 1, fluidTank.getFluid() != null ? fluidTank.getFluidAmount() : -1);
            player.sendProgressBarUpdate(this, i + 2, fluidTank.getCapacity());
            i++;
        }*/
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCraftingToCrafters(ICrafting player) {
        crafters.add(player);
        //sendTanksToCrafter(player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public void updateProgressBar(int id, int data) {
        /*int tank = id / 3;
        SFluidTank fluidTank = tanks.getTankForStack(tank);
        FluidStack fluidStack = fluidTank.getFluid();
        switch (id % 3){
            case 0:
                if(data == -1){
                    fluidTank.setFluid(null);
                    tanks.setTankInStack(fluidTank, tank);
                    break;
                }
                fluidStack.fluidID = data;
                fluidTank.setFluid(fluidStack);
                tanks.setTankInStack(fluidTank, tank);
                break;
            case 1:
                if(data == -1){
                    fluidTank.setFluid(null);
                    tanks.setTankInStack(fluidTank, tank);
                    break;
                }
                fluidStack.amount = data;
                fluidTank.setFluid(fluidStack);
                tanks.setTankInStack(fluidTank, tank);
                break;
            case 2:
                fluidTank.setCapacity(data);
                tanks.setTankInStack(fluidTank, tank);
                break;
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

    @Override
    public void onInventoryChanged() {
        ItemStack input = tankInterface.getStackInSlot(0);
        ItemStack result = tankInterface.getStackInSlot(1);
        SFluidTank tank = tanks.getTankForStack(selectedTank);
        boolean success = false;
        if(input == null)return;
        if(FluidContainerRegistry.isFilledContainer(input)){
            if(tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                if(!addStackToOutput(new ItemStack(input.getItem().getContainerItem(), 1), false))return;
                tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), true);
                result = new ItemStack(input.getItem().getContainerItem());
                success = true;
            }
        }
        else if(FluidContainerRegistry.isEmptyContainer(input))
        {
            if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME){
                result = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
                tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                success = true;
            }

        }
        if(success) {
            tankInterface.decrStackSize(0, 1);
            addStackToOutput(result, true);
            tanks.setTankInStack(tank, selectedTank);
        }
    }

    private boolean addStackToOutput(ItemStack stack, boolean doPut){
        ItemStack output = tankInterface.getStackInSlot(1);
        if(output == null){
            if(doPut){
                tankInterface.setInventorySlotContents(1, stack);
            }
            return true;
        }
        else if(stack.getItem() == output.getItem() && !(stack.getItem().getHasSubtypes() && stack.getItemDamage() != output.getItemDamage()) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()){
            if(doPut){
                tankInterface.incrStackSize(1, stack.stackSize);
            }
            return true;
        }
        else{
            return false;
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
