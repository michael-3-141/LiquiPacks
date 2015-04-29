package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.common.upgrade.LiquidXPUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.network.message.ChangeLiquidXPOptionsMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GuiLiquidXpUpgrade extends GuiTankOptions{

    private LiquipackStack stack;
    private LiquidXPUpgrade upgrade;
    private int upgradeIndex;

    public GuiLiquidXpUpgrade(EntityPlayer player, int upgradeIndex, ItemStack stack) {
        super(player,
                StatCollector.translateToLocal("liquidxp.mode.0"),
                StatCollector.translateToLocal("liquidxp.mode.1"),
                StatCollector.translateToLocal("liquidxp.mode.2"));

        this.stack = new LiquipackStack(stack);
        this.upgradeIndex = upgradeIndex;
        this.upgrade = LiquidXPUpgrade.fromLiquipackUpgrade(this.stack.getUpgrade(upgradeIndex));
    }

    @Override
    public void actionPerformed() {
        Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquidXPOptionsMessageHandler.ChangeLiquidXPOptionsMessage(upgrade.getTank(), upgrade.getMode()));
    }

    @Override
    public int getTank() {
        return upgrade.getTank();
    }

    @Override
    public void setTank(int tank) {
        upgrade.setTank(tank);
        stack.setUpgrade(upgrade, upgradeIndex);
    }

    @Override
    public int getMode() {
        return upgrade.getMode();
    }

    @Override
    public void setMode(int mode) {
        upgrade.setMode(mode);
        stack.setUpgrade(upgrade, upgradeIndex);
    }

    @Override
    public void onGuiClosed() {
    }
}
