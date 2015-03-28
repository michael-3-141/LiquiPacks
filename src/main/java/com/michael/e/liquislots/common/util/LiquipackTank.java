package com.michael.e.liquislots.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class LiquipackTank extends FluidTank {

    private int slot;

    public LiquipackTank(int capacity) {
        super(capacity);
    }

    public LiquipackTank(FluidStack stack, int capacity) {
        super(stack, capacity);
    }

    public LiquipackTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
    }

    public LiquipackTank(FluidTank tank){
        this(tank.getFluid(), tank.getCapacity());
    }

    public LiquipackTank(int capacity, int slot) {
        super(capacity);
        this.slot = slot;
    }

    public LiquipackTank(FluidStack stack, int capacity, int slot) {
        super(stack, capacity);
        this.slot = slot;
    }

    public LiquipackTank(Fluid fluid, int amount, int capacity, int slot) {
        super(fluid, amount, capacity);
        this.slot = slot;
    }

    public LiquipackTank(FluidTank tank, int slot){
        this(tank.getFluid(), tank.getCapacity());
        this.slot = slot;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Capacity", getCapacity());
        super.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public LiquipackTank readFromNBT(NBTTagCompound nbt) {
        if(!nbt.hasKey("Capacity"))return null;
        capacity = nbt.getInteger("Capacity");
        super.readFromNBT(nbt);
        return this;
    }

    public static LiquipackTank loadFromNBT(NBTTagCompound nbt) {
        if(nbt == null || !nbt.hasKey("Capacity"))return null;
        int capacity = nbt.getInteger("Capacity");
        return new LiquipackTank(capacity).readFromNBT(nbt);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof LiquipackTank && ((LiquipackTank) obj).capacity == capacity && ((LiquipackTank) obj).getFluidAmount() == getFluidAmount()){
            if(((LiquipackTank) obj).fluid == null){
                return this.fluid == null;
            }
            else{
                return this.fluid.equals(((LiquipackTank) obj).fluid);
            }
        }
        return false;
    }

    public LiquipackTank copy() {
        return this.fluid == null || this.getFluid().getFluid() == null ? new LiquipackTank(capacity) : new LiquipackTank(fluid.getFluid(), fluid.amount, capacity);
    }

    public void setFluidAmount(int amount){
        if(this.fluid != null){
            if(amount == 0){
                fluid = null;
            }
            else{
                fluid.amount = amount;
            }
        }
    }

    @Override
    public void setFluid(FluidStack fluid) {
        if(fluid != null && fluid.getFluid() == null && fluid.amount != 0){
            this.fluid = null;
        }
        else {
            this.fluid = fluid;
        }
    }
}
