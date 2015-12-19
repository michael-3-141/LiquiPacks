package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.network.message.PlayerTanksFluidUpdateMessageHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
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
        prevTanks = new LiquipackTank[tanks.getTankCount()];

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

    private LiquipackTank[] prevTanks;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(ICrafting player : crafters){
            int i = 0;
            for(LiquipackTank prevTank : prevTanks){
                LiquipackTank currentTank = tanks.getTank(i);
                if(prevTank != null && !prevTank.equals(currentTank)){
                    //If the fluid type hasn't changed, the new fluid amount can be sent using the container.
                    if(prevTank.areFluidTypesEqual(currentTank)){
                        player.sendProgressBarUpdate(this, i, currentTank.getFluidAmount());
                    }
                    //If the fluid type has changed, a custom packet must be used to send the new fluid type
                    else{
                        Liquislots.INSTANCE.netHandler.sendTo(new PlayerTanksFluidUpdateMessageHandler.PlayerTanksFluidUpdateMessage(currentTank.getFluid(), i), (EntityPlayerMP) player);
                    }
                }
                prevTanks[i] = currentTank.copy();
                i++;
            }
        }
    }

    private int tempFluidId;

    @Override
    public void updateProgressBar(int id, int data) {
        if(id % 2 == 0){
            tempFluidId = data;
        }
        else if(id % 2 == 1){
            int tankId = id/2;
            LiquipackTank tank = tanks.getTank(tankId);
            tank.setFluid(new FluidStack(FluidRegistry.getFluid(tempFluidId), data));
            tanks.setTank(tank, tankId);
        }
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
        if(tankInterface.getStackInSlot(0) == null)return;
        ItemStack input = ItemStack.copyItemStack(tankInterface.getStackInSlot(0));
        input.stackSize = 1;
        ItemStack result = ItemStack.copyItemStack(tankInterface.getStackInSlot(1));
        LiquipackTank tank = tanks.getTank(selectedTank);
        boolean success = false;
        if(FluidContainerRegistry.isFilledContainer(input)){
            if(tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                if(!addStackToOutput(input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null, false))return;
                tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), true);
                result = input.getItem().hasContainerItem(input) ? input.getItem().getContainerItem(input) : null;
                success = true;

            }
        }
        else if(FluidContainerRegistry.isEmptyContainer(input))
        {
            if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME){
                if(!addStackToOutput(FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input), false))return;
                result = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
                tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                success = true;
            }

        }
        if(success) {
            tankInterface.getStackInSlot(0).stackSize--;
            if(tankInterface.getStackInSlot(0).stackSize == 0)tankInterface.stacks[0] = null;
            tanks.setTank(tank, selectedTank);
            addStackToOutput(result, true);
        }
    }

    private boolean addStackToOutput(ItemStack stack, boolean doPut){
        ItemStack output = tankInterface.getStackInSlot(1);
        if(stack == null){
            if(doPut)tankInterface.markDirty();
            return true;
        }
        if(output == null){
            if(doPut){
                tankInterface.setInventorySlotContents(1, stack);
            }
            return true;
        }
        else if(stack.isItemEqual(output) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()){
            if(doPut){
                tankInterface.incrStackSize(1, stack.stackSize > 0 ? stack.stackSize : 1, true);
            }
            return true;
        }
        else{
            return false;
        }
    }

    private class InventoryTankInterface implements IInventory {

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
        public ItemStack removeStackFromSlot(int index) {
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
        public void openInventory(EntityPlayer player) {

        }

        @Override
        public void closeInventory(EntityPlayer player) {

        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack stack) {
            return true;
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

        public boolean incrStackSize(int i, int j, boolean markDirty) {
            ItemStack itemstack = getStackInSlot(i);

            if(itemstack != null) {
                if (itemstack.stackSize >= itemstack.getMaxStackSize()) {
                    itemstack.stackSize = itemstack.getMaxStackSize();
                    stacks[i] = itemstack;
                    if(markDirty)markDirty();
                } else {
                    itemstack.stackSize += j;
                    stacks[i] = itemstack;
                    if(markDirty)markDirty();
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public IChatComponent getDisplayName() {
            return null;
        }
    }

    public static class BucketSlot extends Slot {

        public BucketSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return FluidContainerRegistry.isContainer(stack);
        }
    }

    public static class BucketResultSlot extends Slot {

        public BucketResultSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
