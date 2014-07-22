package com.michael.e.liquislots.common;

import com.michael.e.liquislots.item.ItemLiquipack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidTank;

public class TankStack {

    private ItemStack stack;

    public TankStack(ItemStack stack, int tankCount) {
        this.stack = stack;

        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("tanks")){
            NBTTagList nbtTanks = new NBTTagList();
            for(int i = 0; i < tankCount; i++){
                FluidTank tank = new FluidTank(ItemLiquipack.tankCapacities[stack.getItemDamage()]);
                NBTTagCompound compound = new NBTTagCompound();
                tank.writeToNBT(compound);
                nbtTanks.appendTag(compound);
            }
            stack.getTagCompound().setTag("tanks", nbtTanks);
        }
    }

    public TankStack(ItemStack stack){
        this(stack, 2);
    }

    public FluidTank getTankForStack(int tank)
    {
        return (new FluidTank(ItemLiquipack.tankCapacities[stack.getItemDamage()])).readFromNBT(stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(tank));
    }

    public FluidTank[] getTanks(){
        FluidTank[] tanks = new FluidTank[stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).tagCount()];
        for(int i = 0; i < tanks.length; i++){
            tanks[i] = new FluidTank(0);
            tanks[i].readFromNBT(stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i));
        }
        return tanks;
    }

    public ItemStack setTankInStack(FluidTank tank, int tankIndex)
    {
        NBTTagCompound compound = new NBTTagCompound();
        tank.writeToNBT(compound);
        stack.getTagCompound().getTagList("tanks", Constants.NBT.TAG_COMPOUND).func_150304_a(tankIndex, compound);
        return stack;
    }
}
