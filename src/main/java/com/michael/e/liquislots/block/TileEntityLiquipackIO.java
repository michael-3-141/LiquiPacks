package com.michael.e.liquislots.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityLiquipackIO extends TileEntity implements IFluidHandler{

    public FluidTank buffer;

    public TileEntityLiquipackIO() {
        buffer = new FluidTank(10000);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return buffer.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return buffer.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return buffer.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return buffer.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{buffer.getInfo()};
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound nbtBuffer = new NBTTagCompound();
        buffer.writeToNBT(nbtBuffer);
        compound.setTag("buffer", nbtBuffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound nbtBuffer = compound.getCompoundTag("buffer");
        buffer.readFromNBT(nbtBuffer);
    }
}
