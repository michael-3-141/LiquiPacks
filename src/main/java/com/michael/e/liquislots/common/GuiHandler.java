package com.michael.e.liquislots.common;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiPlayerTanks;
import com.michael.e.liquislots.client.gui.GuiTankOptions;
import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackWorkbench;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new ContainerPlayerTanks(player);
            case 1:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackIO)
                return new ContainerLiquipackIO(player, (TileEntityLiquipackIO) world.getTileEntity(x, y, z));
            case 2:
                return new ContainerLiquipackBucketOptions(player, player.getHeldItem());
            case 3:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackWorkbench)
                    return new ContainerLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(x, y, z), player);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new GuiPlayerTanks(player);
            case 1:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackIO)
                return new GuiTankOptions(player, (TileEntityLiquipackIO) world.getTileEntity(x, y, z));
            case 2:
                return new GuiTankOptions(player, player.getHeldItem());
            case 3:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackWorkbench)
                    return new GuiLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(x, y, z), player);
            default:
                return null;
        }
    }

}
