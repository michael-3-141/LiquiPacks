package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerLiquipackWorkbench extends Container {

    TileEntityLiquipackWorkbench tileEntity;

    public ContainerLiquipackWorkbench(TileEntityLiquipackWorkbench tileEntity, EntityPlayer player){
        this.tileEntity = tileEntity;


    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
