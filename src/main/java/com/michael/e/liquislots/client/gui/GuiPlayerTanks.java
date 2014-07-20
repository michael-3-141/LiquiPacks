package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.TankStack;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import com.michael.e.liquislots.network.SelectedTankChangeMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPlayerTanks extends GuiContainer{

    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/tankinv.png");
    private EntityPlayer player;
    private TankStack tanks;

    private GuiTank[] guiTanks = new GuiTank[2];

    private int selectedTank = 0;

    public GuiPlayerTanks(EntityPlayer player) {
        super(new ContainerPlayerTanks(player));
        this.player = player;
        tanks = new TankStack(player.inventory.armorItemInSlot(2));
        xSize = 176;
        ySize = 189;

        guiTanks[0] = new GuiTank(23, 14, 16, 58);
        guiTanks[1] = new GuiTank(53, 14, 16, 58);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        for(int i = 0; i < guiTanks.length; i++) {
            float level = ((float) tanks.getTankForStack(i).getFluidAmount()) / ((float) tanks.getTankForStack(i).getCapacity()) * 58;
            guiTanks[i].render(tanks.getTankForStack(i).getFluid(), (int) level, guiLeft, guiTop);
        }

        mc.renderEngine.bindTexture(texture);
        int i = 0;
        for(GuiTank tank : guiTanks){
            if(i == selectedTank || tank.isMouseInBounds(x-guiLeft,y-guiTop)){
                drawTexturedModalRect(tank.getX() + guiLeft - 3, tank.getY() + guiTop - 3, xSize, 0, 22, 64);
            }
            i++;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {

    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        super.mouseClicked(x, y, btn);
        int i = 0;
        for(GuiTank tank : guiTanks){
            if(tank.isMouseInBounds(x-guiLeft,y-guiTop)){
                selectedTank = i;
                ((ContainerPlayerTanks)inventorySlots).selectedTank = i;
                Liquislots.INSTANCE.netHandler.sendToServer(new SelectedTankChangeMessageHandler.SelectedTankChangeMessage(selectedTank));
                break;
            }
            i++;
        }
    }
}
