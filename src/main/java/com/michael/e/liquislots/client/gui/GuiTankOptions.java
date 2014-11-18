package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiTankOptions extends GuiContainer{

    private static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackIO.png");
    private GuiArrowButton btnBack;
    private GuiArrowButton btnNext;
    private GuiToggleButton btnToggle;
    private GuiMode mode;

    public GuiTankOptions(EntityPlayer player, GuiMode mode, Container container){
        super(container);
        this.mode = mode;

        xSize = 176;
        ySize = 189;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        btnBack = new GuiArrowButton(1, guiLeft + 10, guiTop + 20, false);
        btnNext = new GuiArrowButton(2, guiLeft + 60, guiTop + 20, true);
        btnToggle = new GuiToggleButton(3, guiLeft + 10, guiTop + 50, 20, mode.toggleOptionA, mode.toggleOptionB);

        buttonList.add(btnBack);
        buttonList.add(btnNext);
        buttonList.add(btnToggle);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        fontRendererObj.drawString("Tank:", guiLeft + 28, guiTop + 10, 4210752);
        fontRendererObj.drawString(Integer.toString(mode.getTank()+1), guiLeft + 38, guiTop + 25, 4210752);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == btnBack.id){
            mode.setTank(mode.getTank() - 1);
        }
        else if(button.id == btnNext.id){
            mode.setTank(mode.getTank() + 1);
        }
        else if(button.id == btnToggle.id){
            btnToggle.actionPerfomed();
            mode.setDrainingMode(btnToggle.getState());
        }

        mode.actionPerformed();
        refreshButtons();
    }

    private void refreshButtons(){
        if(mode.getTank() == 0){
            btnBack.enabled = false;
            btnNext.enabled = true;
        }
        else if(mode.getTank() == 3){
            btnBack.enabled = true;
            btnNext.enabled = false;
        }
        else{
            btnBack.enabled = true;
            btnNext.enabled = true;
        }

        btnToggle.setState(mode.isDrainingMode());
    }

    public abstract static class GuiMode {

        public String toggleOptionA;
        public String toggleOptionB;

        public GuiMode(String toggleOptionA, String toggleOptionB) {
            this.toggleOptionA = toggleOptionA;
            this.toggleOptionB = toggleOptionB;
        }

        public void actionPerformed(){}

        public int getTank(){return 0;}

        public void setTank(int tank){}

        public boolean isDrainingMode(){return false;}

        public void setDrainingMode(boolean drainingMode) {}
    }
}
