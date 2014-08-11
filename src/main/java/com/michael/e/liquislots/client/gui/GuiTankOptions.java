package com.michael.e.liquislots.client.gui;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import com.michael.e.liquislots.item.ItemLiquipackBucket;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionsMessageHandler;
import com.michael.e.liquislots.network.message.ChangeTankOptionsMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiTankOptions extends GuiContainer{

    private static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/liquipackIO.png");
    private GuiArrowButton back;
    private GuiArrowButton next;
    private GuiToggleButton mode;
    private TileEntityLiquipackIO te;
    private ItemStack bucket;

    public GuiTankOptions(EntityPlayer player, TileEntityLiquipackIO te) {
        super(new ContainerLiquipackIO(player, te));
        this.te = te;

        xSize = 176;
        ySize = 189;
    }

    public GuiTankOptions(EntityPlayer player, ItemStack bucket) {
        super(new ContainerLiquipackBucketOptions(player, bucket));
        this.bucket = bucket;

        xSize = 176;
        ySize = 189;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        back = new GuiArrowButton(1, guiLeft + 50, guiTop + 25, false);
        next = new GuiArrowButton(2, guiLeft + 100, guiTop + 25, true);
        mode = new GuiToggleButton(3, guiLeft + (te != null ? 42 : 7), guiTop + 50, te != null ? 80 : 160, 20, te != null ? "Drain liquipack" : StatCollector.translateToLocal("liquipackbucket.mode.1"),  te != null ? "Fill liquipack" : StatCollector.translateToLocal("liquipackbucket.mode.0"));

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
        fontRendererObj.drawString(Integer.toString(getTank()+1), guiLeft + 78, guiTop + 30, 4210752);

        refreshButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == back.id){
            setTank(getTank()-1);
        }
        else if(button.id == next.id){
            setTank(getTank()+1);
        }
        else if(button.id == mode.id){
            mode.actionPerfomed();
            setDrainingMode(mode.getState());
        }

        refreshButtons();

        if(te != null){Liquislots.INSTANCE.netHandler.sendToServer(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(te.getTank(), te.isDrainingMode()));}
        else{Liquislots.INSTANCE.netHandler.sendToServer(new ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage(getTank(), isDrainingMode()));}
    }

    private void refreshButtons(){
        if(getTank() == 0){
            back.enabled = false;
            next.enabled = true;
        }
        else if(getTank() == 3){
            back.enabled = true;
            next.enabled = false;
        }
        else{
            back.enabled = true;
            next.enabled = true;
        }

        mode.setState(isDrainingMode());
    }
    
    private int getTank(){
        if(te != null){
            return te.getTank();
        }
        else{
            return ItemLiquipackBucket.getSelectedTank(bucket);
        }
    }
    
    private void setTank(int tank){
        if(te != null){
            te.setTank(tank);
        }
        else{
            ItemLiquipackBucket.setSelectedTank(bucket, tank);
        }
    }

    private boolean isDrainingMode(){
        if(te != null){
            return te.isDrainingMode();
        }
        else{
            return ItemLiquipackBucket.isDrainingMode(bucket);
        }
    }

    private void setDrainingMode(boolean drainingMode){
        if(te != null){
            te.setDrainingMode(drainingMode);
        }
        else{
            ItemLiquipackBucket.setDrainingMode(bucket, drainingMode);
        }
    }
}
