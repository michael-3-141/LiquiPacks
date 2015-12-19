package com.michael.e.liquislots.common;

import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiPlayerTanks;
import com.michael.e.liquislots.common.container.ContainerLiquipackWorkbench;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new ContainerPlayerTanks(player);
            case 1:
                if(world.getTileEntity(new BlockPos(x,y,z)) instanceof TileEntityLiquipackWorkbench)
                    return new ContainerLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(new BlockPos(x,y,z)), player);
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
                if(world.getTileEntity(new BlockPos(x,y,z)) instanceof TileEntityLiquipackWorkbench)
                    return new GuiLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(new BlockPos(x,y,z)), player);
            default:
                return null;
        }
    }
}
