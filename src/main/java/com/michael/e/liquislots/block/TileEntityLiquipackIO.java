package com.michael.e.liquislots.block;

import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemLiquipack;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionsMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public class TileEntityLiquipackIO extends TileEntity implements IFluidHandler{

    public LiquipackTank buffer;
    private int tank;
    private boolean isDrainingMode;
    public ContainerLiquipackIO container;

    public TileEntityLiquipackIO() {
        buffer = new LiquipackTank(10000);
        tank = 0;
        isDrainingMode = true;
        container = new ContainerLiquipackIO();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int result = buffer.fill(resource, doFill);;
        if(doFill){
            sendUpdate();
        }
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        FluidStack result = buffer.drain(resource.amount, doDrain);
        if(doDrain){
            sendUpdate();
        }
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack result = buffer.drain(maxDrain, doDrain);
        if(doDrain){
            sendUpdate();
        }
        return result;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return buffer.getFluidType() == fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return buffer.getFluid() != null;
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
                LiquipackStack liquipack = new LiquipackStack(stack);
                LiquipackTank liquipackTank = liquipack.getTank(this.tank);
                if(liquipackTank == null)return;
                if(isDrainingMode) {
                    if (liquipackTank.getFluid() != null) {
                        int left = liquipackTank.getFluid().amount - ((TileEntityLiquipackIO) tile).buffer.fill(liquipackTank.getFluid(), true);
                        liquipackTank.setFluid(left == 0 ? null : new FluidStack(liquipackTank.getFluidType(), left));
                        liquipack.setTank(liquipackTank, this.tank);
                    }
                }
                else{
                    if(liquipackTank.fill(buffer.getFluid(), false) > 0){
                        int left = buffer.getFluid().amount - liquipackTank.fill(buffer.getFluid(), true);
                        buffer.setFluid(left == 0 ? null : new FluidStack(buffer.getFluid(), left));
                        liquipack.setTank(liquipackTank, this.tank);
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

    private void sendUpdate(){
        container.sendUpdateToPlayers(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(this));
    }
}
