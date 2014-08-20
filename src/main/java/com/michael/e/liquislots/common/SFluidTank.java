package com.michael.e.liquislots.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class SFluidTank extends FluidTank {

    public SFluidTank(int capacity) {
        super(capacity);
    }

    public SFluidTank(FluidStack stack, int capacity) {
        super(stack, capacity);
    }

    public SFluidTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
    }

    public SFluidTank(FluidTank tank){
        this(tank.getFluid(), tank.getCapacity());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Capacity", getCapacity());
        super.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public SFluidTank readFromNBT(NBTTagCompound nbt) {
        if(!nbt.hasKey("Capacity"))return null;
        capacity = nbt.getInteger("Capacity");
        super.readFromNBT(nbt);
        return this;
    }

    public static SFluidTank loadFromNBT(NBTTagCompound nbt) {
        if(!nbt.hasKey("Capacity"))return null;
        int capacity = nbt.getInteger("Capacity");
        return new SFluidTank(capacity).readFromNBT(nbt);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof SFluidTank){
            SFluidTank tank = (SFluidTank) obj;
            if(this.capacity == tank.capacity){
                return this.fluid == null && tank.fluid == null || !(this.fluid == null || tank.fluid == null) && this.fluid.fluidID == tank.fluid.fluidID && this.fluid.amount == tank.fluid.amount;
            }

        }
        return false;
    }
}
