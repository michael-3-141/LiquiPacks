package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.util.LiquipackTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ContainerLiquipackIO extends Container {

    private TileEntityLiquipackIO te;

    public ContainerLiquipackIO(TileEntityLiquipackIO te) {
        this.te = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) < 50;
    }

    private int prevTank;
    private boolean prevDrainMode;
    private LiquipackTank prevBuffer;

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        crafters.add(player);
        player.sendProgressBarUpdate(this, 0, te.getTank());
        player.sendProgressBarUpdate(this, 1, te.isDrainingMode() ? 1 : 0);
    }

    @Override
    public void detectAndSendChanges() {
        for(ICrafting player : ((List<ICrafting>)crafters)){
            if(te.getTank() != prevTank) {
                player.sendProgressBarUpdate(this, 0, te.getTank());
            }
            if (te.isDrainingMode() != prevDrainMode){
                player.sendProgressBarUpdate(this, 1, te.isDrainingMode() ? 1 : 0);
            }
            if(!te.buffer.equals(prevBuffer) && te.buffer.getFluid() != null){
                player.sendProgressBarUpdate(this, 2, te.buffer.getFluid().fluidID);
                player.sendProgressBarUpdate(this, 3, te.buffer.getFluid().amount);
            }
            prevTank = te.getTank();
            prevDrainMode = te.isDrainingMode();
            prevBuffer = te.buffer.copy();
            /*player.sendProgressBarUpdate(this, 0, te.getTank());
            player.sendProgressBarUpdate(this, 1, te.isDrainingMode() ? 1 : 0);*/
        }
    }

    private int fluidID = 0;

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id){
            case 0:
                te.setTank(data);
                break;
            case 1:
                te.setDrainingMode(data == 1);
                break;
            case 2:
                fluidID = data;
            case 3:
                te.buffer.setFluid(new FluidStack(fluidID, data));
        }
    }

    public TileEntityLiquipackIO getTe() {
        return te;
    }
}
