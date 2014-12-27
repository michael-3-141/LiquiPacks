package com.michael.e.liquislots.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiToggleButton extends GuiButton{

    private String[] texts;
    private int currentText = 0;

    public GuiToggleButton(int id, int x, int y, int height, String... text) {
        super(id, x, y, 5, height, text[0]);
        this.texts = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString) + 10;
    }

    public void actionPerfomed(){
        setState((currentText + 1) % texts.length);
    }

    public int getState(){
        return currentText;
    }

    public void setState(int state){
        currentText = state;
        this.displayString = texts[currentText];
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString) + 10;
    }
}
