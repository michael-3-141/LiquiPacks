package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionsMessageHandler;
import com.michael.e.liquislots.network.message.LiquipackIOGuiEventMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class GuiLiquipackIO extends GuiTankOptions{

    private TileEntityLiquipackIO te;
    private GuiTank tank;

    public GuiLiquipackIO(EntityPlayer player, TileEntityLiquipackIO te) {
        super(player, "Drain Liquipack", "Fill Liquipack");
        this.te = te;
        this.tank = new GuiTank(126, 21, 16, 58);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void keyTyped(char c, int key) {
        if(key == Keyboard.KEY_ESCAPE || key == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()){
            Liquislots.INSTANCE.netHandler.sendToServer(
                            new LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage(te.xCoord, te.yCoord, te.zCoord, false));
        }
    }

    @Override
    public void actionPerformed() {
        Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(te));
    }

    @Override
    public int getTank() {
        return te.getTank();
    }

    @Override
    public void setTank(int tank) {
        te.setTank(tank);
    }

    @Override
    public int getMode() {
        return te.isDrainingMode() ? 0 : 1;
    }

    @Override
    public void setMode(int mode) {
        te.setDrainingMode(mode == 0);
    }

    @Override
    protected void drawBackgroundLayer() {
        Minecraft.getMinecraft().renderEngine.bindTexture(GuiTankOptions.texture);
        drawTexturedModalRect(guiLeft + 125, guiTop + 20, 176, 0, 18, 60);
        Minecraft.getMinecraft().fontRenderer.drawString("Buffer:", guiLeft + 125, guiTop + 10, 4210752);
        float level = Math.max(te.buffer.getFluidAmount() / ((float) te.buffer.getCapacity()) * 58, 1);
        tank.render(te.buffer.getFluid(), (int) level, guiLeft, guiTop);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        if(tank.isMouseInBounds(x-guiLeft,y-guiTop)){
            FluidStack contents = te.buffer.getFluid();
            List<String> text = new ArrayList<String>();
            text.add(contents == null || contents.getFluid() == null ? "Empty" : (contents.amount + "mb X " + contents.getFluid().getLocalizedName(contents)));
            drawTooltip(text, x - guiLeft, y - guiTop);
        }
    }
}
