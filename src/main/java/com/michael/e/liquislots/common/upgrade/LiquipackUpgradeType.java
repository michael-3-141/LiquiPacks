package com.michael.e.liquislots.common.upgrade;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.common.GuiHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.swing.text.html.parser.Entity;

public enum LiquipackUpgradeType{

    LIQUID_XP(new UpgradeButton(0, 0, 16, 0)){
        @Override
        public void onClicked(EntityPlayer player, World world, int upgradeIndex) {
            if(!world.isRemote) {
                FMLNetworkHandler.openGui(player, Liquislots.INSTANCE, 4, world, upgradeIndex, 0, 0);
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