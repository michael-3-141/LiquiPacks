package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.block.TileEntityLiquipackWorkbench;
import com.michael.e.liquislots.common.container.ContainerLiquipackWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiLiquipackWorkbench extends GuiContainer {

    ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackWorkbench.png");

    public GuiLiquipackWorkbench(TileEntityLiquipackWorkbench tileEntity, EntityPlayer player) {
        super(new ContainerLiquipackWorkbench(tileEntity, player));
        xSize = 203;
        ySize = 170;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
