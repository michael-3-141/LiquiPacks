package com.michael.e.liquislots.client.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiToggleButton extends GuiButton{

    private String textA;
    private String textB;

    public GuiToggleButton(int id, int x, int y, int width, int height, String textA, String textB) {
        super(id, x, y, width, height, textA);
        this.textA = textA;
        this.textB = textB;
    }

    public void actionPerfomed(){
        this.displayString = this.displayString == textA ? textB : textA;
    }

    public boolean getState(){
        return this.displayString == textA;
    }

    public void setState(boolean state){
        this.displayString = state ? textA : textB;
    }
}
