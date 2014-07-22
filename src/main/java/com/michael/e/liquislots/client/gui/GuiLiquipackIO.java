package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiLiquipackIO extends GuiContainer{

    private static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackIO.png");
    private GuiArrowButton back;
    private GuiArrowButton next;
    private GuiToggleButton mode;
    private TileEntityLiquipackIO te;

    public GuiLiquipackIO(EntityPlayer player, TileEntityLiquipackIO te) {
        super(new ContainerLiquipackIO(player, te));
        this.te = te;

        xSize = 176;
        ySize = 189;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        back = new GuiArrowButton(1, guiLeft + 50, guiTop + 25, false);
        next = new GuiArrowButton(2, guiLeft + 100, guiTop + 25, true);
        mode = new GuiToggleButton(3, guiLeft + 42, guiTop + 50, 80, 20, "Drain liquipack", "Fill liquipack");

        buttonList.add(back);
        buttonList.add(next);
        buttonList.add(mode);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        fontRendererObj.drawString("Tank:", guiLeft + 68, guiTop + 17, 4210752);
        fontRendererObj.drawString(Integer.toString(te.getTank()+1), guiLeft + 78, guiTop + 30, 4210752);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == back.id){
            te.setTank(te.getTank()-1);
        }
        else if(button.id == next.id){
            te.setTank(te.getTank()+1);
        }
        else if(button.id == mode.id){
            mode.actionPerfomed();
            te.setDrainingMode(mode.getState());
        }

        refreshButtons();

        Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquipackIOOptionMessageHandler.ChangeLiquipackIOOptionMessage(te.getTank(), te.isDrainingMode()));
    }

    private void refreshButtons(){
        if(te.getTank() == 0){
            back.enabled = false;
            next.enabled = true;
        }
        else if(te.getTank() == 1){
            back.enabled = true;
            next.enabled = false;
        }

        mode.setState(te.isDrainingMode());
    }
}
