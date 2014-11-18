package com.michael.e.liquislots.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiToggleButton extends GuiButton{

    private String textA;
    private String textB;

    public GuiToggleButton(int id, int x, int y, int height, String textA, String textB) {
        super(id, x, y, 5, height, textA);
        this.textA = textA;
        this.textB = textB;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString) + 10;
    }

    public void actionPerfomed(){
        this.displayString = this.displayString == textA ? textB : textA;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString) + 10;
    }

    public boolean getState(){
        return this.displayString == textA;
    }

    public void setState(boolean state){
        this.displayString = state ? textA : textB;
    }
}
