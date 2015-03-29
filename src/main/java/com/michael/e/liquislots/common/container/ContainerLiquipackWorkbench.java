package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemTank;
import com.michael.e.liquislots.item.ItemsRef;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerLiquipackWorkbench extends Container {

    TileEntityLiquipackWorkbench tileEntity;
    LiquipackInventory liquipackInventory;

    public ContainerLiquipackWorkbench(TileEntityLiquipackWorkbench tileEntity, EntityPlayer player){
        this.tileEntity = tileEntity;
        InventoryPlayer invPlayer = player.inventory;
        liquipackInventory = new LiquipackInventory(tileEntity);

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new SafeSlot(invPlayer, i, 8 + i * 18, 146));
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new SafeSlot(invPlayer, j + i * 9 + 9, 8 + j * 18, i * 18 + 88));
            }
        }

        addSlotToContainer(new ArmorSlot(invPlayer, 38, 179, 24, 1));

        addSlotToContainer(new SafeSlot(tileEntity, 0, 79, 14));

        for(int i = 0; i < 4; i++){
            addSlotToContainer(new SafeSlot(liquipackInventory, i, 52 + i * 18, 60));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }

    public class LiquipackInventory implements IInventory{

        TileEntityLiquipackWorkbench tileEntity;

        public LiquipackInventory(TileEntityLiquipackWorkbench workbench){
            this.tileEntity = workbench;
        }

        private LiquipackStack getStack(){
            return tileEntity.getStackInSlot(0) == null ? null : new LiquipackStack(tileEntity.getStackInSlot(0));
        }

        private ItemStack tankToItemStack(LiquipackTank tank){
            return tank != null ? ItemTank.setTankForStack(new ItemStack(ItemsRef.tank, 1), tank) : null;
        }

        private LiquipackTank itemStackToTank(ItemStack stack){
            if(stack == null)return null;
            return stack.getItem() instanceof ItemTank ? new LiquipackTank(ItemTank.getTankForStack(stack)) : null;
        }

        @Override
        public int getSizeInventory() {
            return 4;
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            LiquipackStack stack = getStack();
            return stack == null ? null : tankToItemStack(stack.getTank(i));
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
            LiquipackStack lpstack = getStack();
            if(lpstack != null){
                lpstack.setTank(itemStackToTank(stack), i);
            }
        }

        @Override
        public String getInventoryName() {
            return "liquipackWorkbenchContents";
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
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return tileEntity.isUseableByPlayer(player);
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack stack) {
            return tileEntity.getStackInSlot(0) != null && itemStackToTank(stack) != null;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();

            if (i >= 36) {
                if (!mergeItemStack(stack, 0, 37, false)) {
                    return null;
                }
            }else if(!mergeItemStack(stack, 37, 42, false)) {
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
    protected boolean mergeItemStack(ItemStack itemStack, int slotMin, int slotMax, boolean ascending)
    {
        boolean slotFound = false;
        int slotIndex = ascending ? slotMax - 1 : slotMin;

        Slot slot;
        ItemStack slotStack;

        if (itemStack.isStackable())
        {
            while (itemStack.stackSize > 0 && (!ascending && slotIndex < slotMax || ascending && slotIndex >= slotMin))
            {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                slotStack = slot.getStack();

                if (slot.isItemValid(itemStack) && slotStack != null && itemStack.getItem() == slotStack.getItem())
                {
                    int combinedStackSize = slotStack.stackSize + itemStack.stackSize;
                    int slotStackSizeLimit = Math.min(slotStack.getMaxStackSize(), slot.getSlotStackLimit());

                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        itemStack.stackSize = 0;
                        slotStack.stackSize = combinedStackSize;
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                    else if (slotStack.stackSize < slotStackSizeLimit)
                    {
                        itemStack.stackSize -= slotStackSizeLimit - slotStack.stackSize;
                        slotStack.stackSize = slotStackSizeLimit;
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }

                slotIndex += ascending ? -1 : 1;
            }
        }

        if (itemStack.stackSize > 0)
        {
            slotIndex = ascending ? slotMax - 1 : slotMin;

            while (!ascending && slotIndex < slotMax || ascending && slotIndex >= slotMin)
            {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                slotStack = slot.getStack();

                if (slot.isItemValid(itemStack) && slotStack == null)
                {
                    slot.putStack(itemStack.copy());
                    slot.onSlotChanged();

                    if (slot.getStack() != null)
                    {
                        itemStack.stackSize -= slot.getStack().stackSize;
                        slotFound = true;
                    }

                    break;
                }

                slotIndex += ascending ? -1 : 1;
            }
        }
        return slotFound;
    }

    public class SafeSlot extends Slot{

        public SafeSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return inventory.isItemValidForSlot(getSlotIndex(), stack);
        }
    }

    public class ArmorSlot extends Slot {

        private int armorType;

        public ArmorSlot(IInventory inventory, int index, int x, int y, int armorType) {
            super(inventory, index, x, y);
            this.armorType = armorType;
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
         * in the case of armor slots)
         */
        public int getSlotStackLimit()
        {
            return 1;
        }
        /**
         * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
         */
        public boolean isItemValid(ItemStack p_75214_1_)
        {
            if (p_75214_1_ == null) return false;
            return p_75214_1_.getItem().isValidArmor(p_75214_1_, armorType, null);
        }
        /**
         * Returns the icon index on items.png that is used as background image of the slot.
         */
        @SideOnly(Side.CLIENT)
        public IIcon getBackgroundIconIndex()
        {
            return ItemArmor.func_94602_b(armorType);
        }
    }
}
