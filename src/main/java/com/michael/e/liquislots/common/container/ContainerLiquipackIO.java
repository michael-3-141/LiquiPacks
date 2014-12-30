package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.util.LiquipackTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;

public class ContainerLiquipackIO extends Container {

    private TileEntityLiquipackIO te;

    public ContainerLiquipackIO(EntityPlayer player, TileEntityLiquipackIO te) {
        this.te = te;
        InventoryPlayer invPlayer = player.inventory;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, i * 18 + 107));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 165));
        }
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
