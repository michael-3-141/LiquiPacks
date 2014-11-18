package com.michael.e.liquislots.common;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiLiquipackWorkbench;
import com.michael.e.liquislots.client.gui.GuiPlayerTanks;
import com.michael.e.liquislots.client.gui.GuiTankOptions;
import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackWorkbench;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import com.michael.e.liquislots.item.ItemLiquipackBucket;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionsMessageHandler;
import com.michael.e.liquislots.network.message.ChangeTankOptionsMessageHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
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
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackIO) {
                    TileEntityLiquipackIO te = (TileEntityLiquipackIO) world.getTileEntity(x, y, z);
                    return new GuiTankOptions(player, new GuiModeIO(te), new ContainerLiquipackIO(player, te));
                }
            case 2:
                return new GuiTankOptions(player, new GuiModeLiquipackBucket(player.getHeldItem()), new ContainerLiquipackBucketOptions(player, player.getHeldItem()));
            case 3:
                if(world.getTileEntity(x, y, z) instanceof TileEntityLiquipackWorkbench)
                    return new GuiLiquipackWorkbench((TileEntityLiquipackWorkbench) world.getTileEntity(x, y, z), player);
            default:
                return null;
        }
    }

    public static class GuiModeIO extends GuiTankOptions.GuiMode{

        private TileEntityLiquipackIO te;

        public GuiModeIO(TileEntityLiquipackIO te) {
            super("Drain Liquipack", "Fill Liquipack");
            this.te = te;
        }

        @Override
        public void actionPerformed() {
            Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(te.getTank(), te.isDrainingMode()));
        }

        @Override
        public int getTank() {
            return te.getTank();
        }

        @Override
        public void setTank(int tank) {
            te.setTank(tank);
        }

        @Override
        public boolean isDrainingMode() {
            return te.isDrainingMode();
        }

        @Override
        public void setDrainingMode(boolean drainingMode) {
            te.setDrainingMode(drainingMode);
        }
    }

    public static class GuiModeLiquipackBucket extends GuiTankOptions.GuiMode{

        private ItemStack stack;

        public GuiModeLiquipackBucket(ItemStack stack) {
            super(StatCollector.translateToLocal("liquipackbucket.btnToggle.1"), StatCollector.translateToLocal("liquipackbucket.btnToggle.0"));
            this.stack = stack;
        }

        @Override
        public void actionPerformed() {
            Liquislots.INSTANCE.netHandler.sendToServer(new ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage(ItemLiquipackBucket.getSelectedTank(stack), ItemLiquipackBucket.isDrainingMode(stack)));
        }

        @Override
        public int getTank() {
            return ItemLiquipackBucket.getSelectedTank(stack);
        }

        @Override
        public void setTank(int tank) {
            ItemLiquipackBucket.setSelectedTank(stack, tank);
        }

        @Override
        public boolean isDrainingMode() {
            return ItemLiquipackBucket.isDrainingMode(stack);
        }

        @Override
        public void setDrainingMode(boolean drainingMode) {
            ItemLiquipackBucket.setDrainingMode(stack, drainingMode);
        }

    }
}
