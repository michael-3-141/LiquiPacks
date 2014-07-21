package com.michael.e.liquislots.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiArrowButton extends GuiButton
{
    private final boolean field_146157_o;
    private static ResourceLocation texture = new ResourceLocation("textures/gui/container/villager.png");

    public GuiArrowButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_)
    {
        super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
        this.field_146157_o = p_i1095_4_;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
            p_146112_1_.getTextureManager().bindTexture(texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = 0;
            int l = 176;

            if (!this.enabled)
            {
                l += this.width * 2;
            }
            else if (flag)
            {
                l += this.width;
            }

            if (!this.field_146157_o)
            {
                k += this.height;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
        }
    }
}

