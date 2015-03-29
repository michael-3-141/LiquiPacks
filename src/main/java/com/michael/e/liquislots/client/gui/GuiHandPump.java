package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.item.ItemHandPump;
import com.michael.e.liquislots.network.message.ChangeTankOptionsMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GuiHandPump extends GuiTankOptions{
    private ItemStack stack;

    public GuiHandPump(EntityPlayer player, ItemStack stack) {
        super(player, new ContainerLiquipackBucketOptions(stack),
                StatCollector.translateToLocal("liquipackbucket.mode.0"),
                StatCollector.translateToLocal("liquipackbucket.mode.1"),
                StatCollector.translateToLocal("liquipackbucket.mode.2"));
        this.stack = stack;
    }

    @Override
    public void actionPerformed() {
        Liquislots.INSTANCE.netHandler.sendToServer(new ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage(ItemHandPump.getSelectedTank(stack), ItemHandPump.getMode(stack)));
    }

    @Override
    public int getTank() {
        return ItemHandPump.getSelectedTank(stack);
    }

    @Override
    public void setTank(int tank) {
        ItemHandPump.setSelectedTank(stack, tank);
    }

    @Override
    public int getMode() {
        return ItemHandPump.getMode(stack);
    }

    @Override
    public void setMode(int mode) {
        ItemHandPump.setMode(stack, mode);
    }
}
