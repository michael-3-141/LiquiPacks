package com.michael.e.liquislots.common.upgrade;

import com.michael.e.liquislots.client.gui.GuiLiquidXpUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import openblocks.OpenBlocks;
import openmods.utils.EnchantmentUtils;

public enum LiquipackUpgradeType{

    LIQUID_XP(new UpgradeButton(0, 0, 16, 0)){
        @Override
        public void onClicked(EntityPlayer player, World world, int upgradeIndex) {
            if(world.isRemote) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiLiquidXpUpgrade(player, upgradeIndex, player.inventory.armorItemInSlot(2)));
            }
        }

        @Override
        public void tick(World world, EntityPlayer player, LiquipackStack stack, LiquipackUpgrade upgrade) {
            LiquidXPUpgrade liquidXPUpgrade = LiquidXPUpgrade.fromLiquipackUpgrade(upgrade);
            LiquipackTank tank = stack.getTank(liquidXPUpgrade.getTank());
            if(tank != null) {
                if(liquidXPUpgrade.getMode() == LiquidXPUpgrade.MODE_DRAIN_XP) {
                    int liquidAmount = EnchantmentUtils.XPToLiquidRatio(EnchantmentUtils.getPlayerXP(player));
                    if(liquidAmount == 0)return;
                    FluidStack xpStack = OpenBlocks.XP_FLUID.copy();
                    xpStack.amount = liquidAmount;
                    int filled = tank.fill(xpStack, true);
                    EnchantmentUtils.addPlayerXP(player, -EnchantmentUtils.liquidToXPRatio(filled));
                    stack.setTank(tank, liquidXPUpgrade.getTank());
                }
                else if(liquidXPUpgrade.getMode() == LiquidXPUpgrade.MODE_DRAIN_TANK){
                    int liquidAmount = tank.getFluidAmount();
                    EnchantmentUtils.addPlayerXP(player, EnchantmentUtils.liquidToXPRatio(liquidAmount));
                    tank.setFluidAmount(0);
                    stack.setTank(tank, liquidXPUpgrade.getTank());
                }
            }
        }
    };

    private UpgradeButton button;

    LiquipackUpgradeType(UpgradeButton button) {
        this.button = button;
    }

    public UpgradeButton getButton() {
        return button;
    }

    public void setButton(UpgradeButton button) {
        this.button = button;
    }

    public void onClicked(EntityPlayer player, World world, int upgradeIndex){}

    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        compound.setString("type", this.name());
        return compound;
    }

    public static LiquipackUpgradeType loadFromNBT(NBTTagCompound compound){
        return compound == null ? null : LiquipackUpgradeType.valueOf(compound.getString("type"));
    }

    public void tick(World world, EntityPlayer player, LiquipackStack stack, LiquipackUpgrade upgrade) {

    }

    public static class UpgradeButton{
        private int buttonX;
        private int buttonY;
        private int pressedButtonX;
        private int pressedButtonY;

        public UpgradeButton(int buttonX, int buttonY, int pressedButtonX, int pressedButtonY) {
            this.buttonX = buttonX;
            this.buttonY = buttonY;
            this.pressedButtonX = pressedButtonX;
            this.pressedButtonY = pressedButtonY;
        }

        public int getButtonX() {
            return buttonX;
        }

        public int getButtonY() {
            return buttonY;
        }

        public int getPressedButtonX() {
            return pressedButtonX;
        }

        public int getPressedButtonY() {
            return pressedButtonY;
        }
    }
}