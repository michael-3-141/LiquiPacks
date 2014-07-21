package com.michael.e.liquislots.client.renderers;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.client.models.ModelLiquipackIO;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import org.lwjgl.opengl.GL11;

public class LiquipackIORenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{

    private ModelLiquipackIO model = new ModelLiquipackIO();
    private ResourceLocation modelTexture = new ResourceLocation(Reference.MOD_ID, "models/liquipackIO.png");
    public static int rendererID;

    public LiquipackIORenderer() {
        rendererID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glRotated(180, 1, 0, 1);
        bindTexture(modelTexture);
        model.render(null, 1F, 0F, 0F, 0F, 0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return rendererID;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTickTime) {
        World world = tileEntity.getWorldObj();
        int tileX = tileEntity.xCoord;
        int tileY = tileEntity.yCoord;
        int tileZ = tileEntity.zCoord;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x+0.5F, (float)y+0.03125F, (float)z+0.5F);
        GL11.glRotated(180, 1, 0, 1);
        bindTexture(modelTexture);
        boolean dir1 = world.getTileEntity(tileX + 1, tileY, tileZ) instanceof IFluidHandler;
        boolean dir2 = world.getTileEntity(tileX, tileY, tileZ + 1) instanceof IFluidHandler;
        boolean dir3 = world.getTileEntity(tileX - 1, tileY, tileZ) instanceof IFluidHandler;
        boolean dir4 = world.getTileEntity(tileX, tileY, tileZ - 1) instanceof IFluidHandler;
        model.render(null, dir1 ? 1F : 0F, dir2 ? 1F : 0F, dir3 ? 1F : 0F, dir4 ? 1F : 0F, 0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
