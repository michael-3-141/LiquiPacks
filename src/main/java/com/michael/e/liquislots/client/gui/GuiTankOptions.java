package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiTankOptions extends GuiContainer{

    public static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackIO.png");
    private GuiArrowButton btnBack;
    private GuiArrowButton btnNext;
    private GuiToggleButton btnToggle;

    protected String[] toggleOptions;

    public GuiTankOptions(EntityPlayer player, Container container, String... toggleOptions){
        super(container);
        this.toggleOptions = toggleOptions;

        xSize = 176;
        ySize = 189;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        btnBack = new GuiArrowButton(1, guiLeft + 10, guiTop + 20, false);
        btnNext = new GuiArrowButton(2, guiLeft + 60, guiTop + 20, true);
        btnToggle = new GuiToggleButton(3, guiLeft + 10, guiTop + 50, 20, toggleOptions);

        buttonList.add(btnBack);
        buttonList.add(btnNext);
        buttonList.add(btnToggle);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawBackgroundLayer();
        fontRendererObj.drawString("Tank:", guiLeft + 28, guiTop + 10, 4210752);
        fontRendererObj.drawString(Integer.toString(getTank()+1), guiLeft + 38, guiTop + 25, 4210752);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == btnBack.id){
            setTank(getTank() - 1);
        }
        else if(button.id == btnNext.id){
            setTank(getTank() + 1);
        }
        else if(button.id == btnToggle.id){
            btnToggle.actionPerfomed();
            setMode(btnToggle.getState());
        }

        actionPerformed();
        refreshButtons();
    }

    private void refreshButtons(){
        if(getTank() == 0){
            btnBack.enabled = false;
            btnNext.enabled = true;
        }
        else if(getTank() == 3){
            btnBack.enabled = true;
            btnNext.enabled = false;
        }
        else{
            btnBack.enabled = true;
            btnNext.enabled = true;
        }

        btnToggle.setState(getMode());
    }

    protected void drawTooltip(List text, int x, int y){
        drawHoveringText(text, x, y, fontRendererObj);
    }

    protected void actionPerformed(){}
    
    protected int getTank(){return 0;}

    protected void setTank(int tank){}

    protected int getMode(){return 0;}

    protected void setMode(int mode) {}

    protected void drawBackgroundLayer() {}
}
