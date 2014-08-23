package com.michael.e.liquislots.common;

import com.michael.e.liquislots.item.ILiquipackArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class LiquipackStack {

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
}
