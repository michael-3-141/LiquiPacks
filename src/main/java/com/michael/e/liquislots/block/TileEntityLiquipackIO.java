package com.michael.e.liquislots.block;

import com.michael.e.liquislots.common.TankStack;
import com.michael.e.liquislots.item.ItemLiquipack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileEntityLiquipackIO extends TileEntity implements IFluidHandler{

    public FluidTank buffer;
    private int tank;
    private boolean isDrainingMode;

    public TileEntityLiquipackIO() {
        buffer = new FluidTank(10000);
        tank = 0;
        isDrainingMode = true;
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
        if(buffer.getFluid() != null) {
            buffer.writeToNBT(nbtBuffer);
        }
        compound.setTag("buffer", nbtBuffer);
        compound.setInteger("tank", tank);
        compound.setBoolean("isDrainingMode", isDrainingMode);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("buffer")) {
            NBTTagCompound nbtBuffer = compound.getCompoundTag("buffer");
            buffer.readFromNBT(nbtBuffer);
        }
        tank = compound.getInteger("tank");
        isDrainingMode = compound.getBoolean("isDrainingMode");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateEntity() {
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
        for(EntityPlayer player : ((List<EntityPlayer>)worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb))){
            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord, zCoord);
            if(player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemLiquipack && tile instanceof TileEntityLiquipackIO){
                ItemStack stack = player.inventory.armorItemInSlot(2);
                TankStack tank = new TankStack(stack);
                FluidTank fluidTank = tank.getTankForStack(this.tank);
                if(isDrainingMode) {
                    if (fluidTank.getFluid() != null) {
                        int left = fluidTank.getFluid().amount - ((TileEntityLiquipackIO) tile).buffer.fill(fluidTank.getFluid(), true);
                        fluidTank.setFluid(new FluidStack(fluidTank.getFluid().getFluid(), left));
                        tank.setTankInStack(fluidTank, this.tank);
                    }
                }
                else{
                    if(fluidTank.fill(buffer.getFluid(), false) > 0){
                        int left = buffer.getFluid().amount - fluidTank.fill(buffer.getFluid(), true);
                        buffer.setFluid(new FluidStack(buffer.getFluidAmount(), left));
                        tank.setTankInStack(fluidTank, this.tank);
                    }
                }
            }
        }
    }

    public int getTank() {
        return tank;
    }

    public void setTank(int tank) {
        this.tank = tank;
    }

    public boolean isDrainingMode() {
        return isDrainingMode;
    }

    public void setDrainingMode(boolean isDrainingMode) {
        this.isDrainingMode = isDrainingMode;
    }  
}
