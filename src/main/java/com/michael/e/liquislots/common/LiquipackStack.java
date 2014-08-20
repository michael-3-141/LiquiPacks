package com.michael.e.liquislots.common;

import com.michael.e.liquislots.item.ILiquipackArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class LiquipackStack implements IInventory{

    private ItemStack stack;

    public LiquipackStack(ItemStack stack, int tankCount, int... capacities) {
        this.stack = stack;

        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("tanks")){
            NBTTagList nbtTanks = new NBTTagList();
            for(int i = 0; i < tankCount; i++){
                SFluidTank tank = new SFluidTank(capacities[stack.getItemDamage()]);
                NBTTagCompound compound = new NBTTagCompound();
                tank.writeToNBT(compound);
                nbtTanks.appendTag(compound);
            }
            stack.getTagCompound().setTag("tanks", nbtTanks);
        }
    }

    public LiquipackStack(ItemStack stack){
        this(stack, 0);
    }

    public SFluidTank getTankForStack(int tank)
    {
        return new SFluidTank(10000).readFromNBT(stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(tank));
    }

    public SFluidTank[] getTanks(){
        SFluidTank[] tanks = new SFluidTank[stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).tagCount()];
        for(int i = 0; i < tanks.length; i++){
            tanks[i] = SFluidTank.loadFromNBT(stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i));
        }
        return tanks;
    }

    public ItemStack setTankInStack(SFluidTank tank, int tankIndex)
    {
        NBTTagCompound compound = new NBTTagCompound();
        tank.writeToNBT(compound);
        stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).func_150304_a(tankIndex, compound);
        return stack;
    }

    public ItemStack setTanks(NBTTagList tanks){
        stack.getTagCompound().setTag("tanks", tanks);
        return stack;
    }

    public ItemStack addTankTankToStack(SFluidTank tank)
    {
        NBTTagCompound compound = new NBTTagCompound();
        tank.writeToNBT(compound);
        stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).appendTag(compound);
        return stack;
    }

    public ItemStack setArmor(ItemStack protection){
        if(!(protection.getItem() instanceof ILiquipackArmor))throw new IllegalArgumentException("Liquipack armor item must implement ILiquipackArmor");
        NBTTagCompound compound = new NBTTagCompound();
        protection.writeToNBT(compound);
        stack.getTagCompound().setTag("armor", compound);
        return stack;
    }

    public ItemStack getArmor(){
        return ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("armor"));
    }

    public ItemStack removeArmor(){
        stack.getTagCompound().removeTag("armor");
        return stack;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return ItemStack.loadItemStackFromNBT(stack.getTagCompound().getTagList("interface", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i));
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
    public ItemStack getStackInSlotOnClosing(int i) {
        return getStackInSlot(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack item) {
        NBTTagCompound nbt = new NBTTagCompound();
        if(item == null)nbt.setString("Empty", "");
        else item.writeToNBT(nbt);
        stack.getTagCompound().getTagList("interface", Constants.NBT.TAG_COMPOUND).func_150304_a(i, nbt);
    }

    @Override
    public String getInventoryName() {
        return null;
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
