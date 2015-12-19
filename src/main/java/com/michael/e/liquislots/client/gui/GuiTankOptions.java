package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

public class GuiTankOptions extends GuiScreen {

    public static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackIO.png");
    private GuiArrowButton btnBack;
    private GuiArrowButton btnNext;
    private GuiToggleButton btnToggle;

    protected int xSize;
    protected int ySize;
    protected int guiLeft;
    protected int guiTop;

    protected String[] toggleOptions;

    public GuiTankOptions(EntityPlayer player, String... toggleOptions){
        this.toggleOptions = toggleOptions;

        xSize = 176;
        ySize = 85;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        btnBack = new GuiArrowButton(1, guiLeft + 10, guiTop + 20, false);
        btnNext = new GuiArrowButton(2, guiLeft + 60, guiTop + 20, true);
        btnToggle = new GuiToggleButton(3, guiLeft + 10, guiTop + 50, 20, toggleOptions);

        buttonList.add(btnBack);
        buttonList.add(btnNext);
        buttonList.add(btnToggle);

        refreshButtons();
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);

        this.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(x, y, f);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)guiLeft, (float)guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        drawGuiContainerForegroundLayer(x, y);

        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer(int x, int y) {}

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawBackgroundLayer();
        fontRendererObj.drawString("Tank:", guiLeft + 28, guiTop + 10, 4210752);
        fontRendererObj.drawString(Integer.toString(getTank()+1), guiLeft + 38, guiTop + 25, 4210752);

        refreshButtons();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
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
