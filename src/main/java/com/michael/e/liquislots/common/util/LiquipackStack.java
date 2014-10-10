package com.michael.e.liquislots.common.util;

import com.michael.e.liquislots.item.ILiquipackArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LiquipackStack {

    private ItemStack stack;

    public LiquipackStack(ItemStack stack){
        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("tanks"))stack.getTagCompound().setTag("tanks", new NBTTagCompound());
        this.stack = stack;
    }

    private NBTNumberedList getList(){
        return new NBTNumberedList(stack.getTagCompound().getCompoundTag("tanks"));
    }

    public LiquipackTank getTank(int tank)
    {
        return LiquipackTank.loadFromNBT(getList().get(tank));
    }

    public LiquipackTank[] getTanks(){
        LiquipackTank[] tanks = new LiquipackTank[4];
        for(int i = 0; i < tanks.length; i++){
            tanks[i] = LiquipackTank.loadFromNBT(getList().get(i));
        }
        return tanks;
    }

    public int getTankCount(){
        int i = 0;
        for(LiquipackTank tank : getTanks()){
            if(tank != null)i++;
        }
        return i;
    }

    public ItemStack setTank(LiquipackTank tank, int tankIndex)
    {
        if(tank != null) {
            NBTTagCompound compound = new NBTTagCompound();
            tank.writeToNBT(compound);
            getList().set(tankIndex, tank.writeToNBT(new NBTTagCompound()));
        }
        else{
            getList().remove(tankIndex);
        }
        return stack;
    }

    public ItemStack addTank(LiquipackTank tank)
    {
        if(tank == null)return stack;
        NBTTagCompound compound = new NBTTagCompound();
        tank.writeToNBT(compound);
        getList().add(tank.writeToNBT(new NBTTagCompound()));
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
