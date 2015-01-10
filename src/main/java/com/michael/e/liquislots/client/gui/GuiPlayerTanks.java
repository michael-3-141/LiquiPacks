package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.upgrade.LiquipackUpgradeType;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import com.michael.e.liquislots.common.upgrade.LiquipackUpgrade;
import com.michael.e.liquislots.network.message.SelectedTankChangeMessageHandler;
import com.michael.e.liquislots.network.message.UpgradeButtonClickMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiPlayerTanks extends GuiContainer{

    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/tankinv.png");
    private EntityPlayer player;
    private LiquipackStack liquipackStack;

    private GuiTank[] guiTanks;
    private GuiUpgradeButton[] guiUpgradeButtons;

    private int selectedTank = 0;

    public GuiPlayerTanks(EntityPlayer player) {
        super(new ContainerPlayerTanks(player));
        this.player = player;
        liquipackStack = new LiquipackStack(player.inventory.armorItemInSlot(2));
        xSize = 176;
        ySize = 189;

        guiTanks = new GuiTank[4];
        for(int i = 0; i < guiTanks.length; i++){
            if(liquipackStack.getTank(i) != null) {
                guiTanks[i] = new GuiTank(36 + (30 * i), 14, 16, 58);
            }
        }

        guiUpgradeButtons = new GuiUpgradeButton[liquipackStack.getUpgradeCount()];
        for(int i = 0; i < guiUpgradeButtons.length; i++){
            guiUpgradeButtons[i] = new GuiUpgradeButton(guiLeft + 156, guiTop + 4 + i*16, i, liquipackStack);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        for(int i = 0; i < guiTanks.length; i++) {
            if(guiTanks[i] != null) {
                float level = ((float) liquipackStack.getTank(i).getFluidAmount()) / ((float) liquipackStack.getTank(i).getCapacity()) * 58;
                guiTanks[i].render(liquipackStack.getTank(i).getFluid(), (int) level, guiLeft, guiTop);
            }
            else {
                mc.renderEngine.bindTexture(texture);
                drawTexturedModalRect(34 + (i * 30) + guiLeft, 12 + guiTop, 0, ySize, 20, 61);
            }
        }

        mc.renderEngine.bindTexture(texture);
        int i = 0;
        for(GuiTank tank : guiTanks){
            if(tank != null && (i == selectedTank || tank.isMouseInBounds(x-guiLeft,y-guiTop))){
                drawTexturedModalRect(tank.getX() + guiLeft - 3, tank.getY() + guiTop - 3, xSize, 0, 22, 64);
            }
            i++;
        }

        for(GuiUpgradeButton button : guiUpgradeButtons){
            button.render(guiLeft, guiTop, x, y);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        int i = 0;
        for(GuiTank tank : guiTanks){
            if(tank != null && tank.isMouseInBounds(x-guiLeft,y-guiTop)){
                FluidStack contents = liquipackStack.getTank(i).getFluid();
                List<String> text = new ArrayList<String>();
                text.add(contents == null ? "Empty" : (contents.amount + "mb X " + contents.getFluid().getLocalizedName(contents)));
                drawHoveringText(text, x-guiLeft, y-guiTop, fontRendererObj);
            }
            i++;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        super.mouseClicked(x, y, btn);
        int i = 0;
        for(GuiTank tank : guiTanks){
            if(tank != null && tank.isMouseInBounds(x-guiLeft,y-guiTop)){
                selectedTank = i;
                ((ContainerPlayerTanks)inventorySlots).selectedTank = i;
                Liquislots.INSTANCE.netHandler.sendToServer(new SelectedTankChangeMessageHandler.SelectedTankChangeMessage(selectedTank));
                ((ContainerPlayerTanks) inventorySlots).onInventoryChanged();
                break;
            }
            i++;
        }

        i = 0;
        for(GuiUpgradeButton button : guiUpgradeButtons){
            if(button.isMouseInBounds(x-guiLeft,y-guiTop)){
                Liquislots.INSTANCE.netHandler.sendToServer(new UpgradeButtonClickMessageHandler.UpgradeButtonClickMessage(button.getUpgradeIndex()));
            }
            i++;
        }
    }
}
