package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.config.ConfigHandler;
import com.michael.e.liquislots.network.message.LiquipackIOGuiEventMessageHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLiquipackIO extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon blockTexture;

    public static int renderID = -1;

    protected BlockLiquipackIO() {
        super(Material.iron);
        setBlockName("liquipackIO");
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        setHardness(2.0F);
        setResistance(10.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockTexture = register.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return blockTexture;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityLiquipackIO();
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote){
            Liquislots.INSTANCE.netHandler.sendToServer(new LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage(x, y, z, true));
        }

        if(!world.isRemote && player.isSneaking() && ConfigHandler.debugMode)
        {
            TileEntityLiquipackIO te = (TileEntityLiquipackIO) world.getTileEntity(x, y, z);
            if(te.buffer.getFluid() != null && te.buffer.getFluidType() != null) {
                player.addChatComponentMessage(new ChatComponentText(te.buffer.getFluidType().getName() + " " + te.buffer.getFluid().amount));
            }else if(te.buffer.getFluid() != null){
                player.addChatComponentMessage(new ChatComponentText(te.buffer.getFluid().getFluidID() + " " + te.buffer.getFluid().amount));
            }
            player.addChatComponentMessage(new ChatComponentText(te.isDrainingMode() + " " + te.getTank()));
        }
        return true;
    }


}
