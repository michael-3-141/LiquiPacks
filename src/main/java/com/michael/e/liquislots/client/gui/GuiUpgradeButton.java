package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.upgrade.LiquipackUpgrade;
import com.michael.e.liquislots.common.upgrade.LiquipackUpgradeType;
import com.michael.e.liquislots.common.util.LiquipackStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiUpgradeButton extends Gui{

    private static final ResourceLocation buttonTexture = new ResourceLocation(Reference.MOD_ID, "textures/gui/upgrades.png");

    private int x;
    private int y;
    private LiquipackUpgradeType.UpgradeButton button;
    private int upgradeIndex;

    public GuiUpgradeButton(int x, int y, int index, LiquipackStack stack) {
        this.x = x;
        this.y = y;
        this.button = stack.getUpgrade(index).getType().getButton();
        this.upgradeIndex = index;
    }

    public void render(int guiLeft, int guiTop, int mouseX, int mouseY){
        Minecraft.getMinecraft().renderEngine.bindTexture(buttonTexture);
        int x = this.x + guiLeft;
        int y = this.y + guiTop;
        if(!isMouseInBounds(mouseX-guiLeft, mouseY-guiTop)) {
            drawTexturedModalRect(x, y, button.getButtonX(), button.getButtonY(), 16, 16);
        }else{
            drawTexturedModalRect(x, y, button.getPressedButtonX(), button.getPressedButtonY(), 16, 16);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public LiquipackUpgradeType.UpgradeButton getButton() {
        return button;
    }

    public void setButton(LiquipackUpgradeType.UpgradeButton button) {
        this.button = button;
    }

    public int getUpgradeIndex() {
        return upgradeIndex;
    }

    public void setUpgradeIndex(int upgradeIndex) {
        this.upgradeIndex = upgradeIndex;
    }

    public boolean isMouseInBounds(int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x+16 && mouseY >= y && mouseY <= y+16;
    }
}
