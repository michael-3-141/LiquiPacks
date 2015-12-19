package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiTank extends Gui {

    private int x;
    private int y;
    private int width;
    private int height;

    private static ResourceLocation tankOverlayTexture = new ResourceLocation(Reference.MOD_ID, "textures/gui/tankOverlay.png");

    public GuiTank(int x, int y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(FluidStack fluid, int level, int guiLeft, int guiTop) {
        /*if (fluid == null || fluid.getFluid() == null) {
            return;
        }
        int x = this.x + guiLeft;
        int y = this.y + guiTop;
        ResourceLocation resourceLocation = fluid.getFluid().getStill();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        setGLColorFromInt(fluid.getFluid().getColor(fluid));
        int fullX = width / 16;
        int fullY = height / 16;
        int lastX = width - fullX * 16;
        int lastY = height - fullY * 16;
        int fullLvl = (height - level) / 16;
        int lastLvl = (height - level) - fullLvl * 16;
        for (int i = 0; i < fullX; i++) {
            for (int j = 0; j < fullY; j++) {
                if (j >= fullLvl) {
                    drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
                }
            }
        }
        for (int i = 0; i < fullX; i++) {
            drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
        }
        for (int i = 0; i < fullY; i++) {
            if (i >= fullLvl) {
                drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
            }
        }
        drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);

        Minecraft.getMinecraft().renderEngine.bindTexture(tankOverlayTexture);
        drawTexturedModalRect(x, y, 0, 0, 16, 58);*/
    }

    /*The magic is here
    private void drawCutIcon(ResourceLocation icon, int x, int y, int width, int height, int cut) {
        WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, renderer.getVertexFormat());
        renderer.addVertexWithUV(x, y + height, 0, icon.getMinU(), icon.getInterpolatedV(height));
        renderer.addVertexWithUV(x + width, y + height, 0, icon.getInterpolatedU(width), icon.getInterpolatedV(height));
        renderer.addVertexWithUV(x + width, y + cut, 0, icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
        renderer.addVertexWithUV(x, y + cut, 0, icon.getMinU(), icon.getInterpolatedV(cut));
        renderer.draw();
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, 1.0F);
    }*/

    public boolean isMouseInBounds(int mouseX, int mouseY){
        return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
