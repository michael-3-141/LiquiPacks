package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockLiquipackWorkbench extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon texture_top;
    private IIcon texture_side;
    private IIcon texture_bottom;
    private IIcon texture_front;

    protected BlockLiquipackWorkbench() {
        super(Material.iron);
        setBlockName("liquipackWorkbench");
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
        setHardness(2.0F);
        setResistance(7.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        texture_top = register.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_top");
        texture_side = register.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_side");
        texture_bottom = register.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_bottom");
        texture_front = register.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_front");
    }

    private static int[] directionToSide = new int[]{2, 5, 3, 4};

    @Override
    public IIcon getIcon(int side, int meta) {
        switch (side){
            case 0:
                return texture_bottom;
            case 1:
                return texture_top;
            default:
                if(side == directionToSide[meta])
                    return texture_front;
                else
                    return texture_side;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityLiquipackWorkbench();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            FMLNetworkHandler.openGui(player, Liquislots.INSTANCE, 3, world, x, y, z);
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        world.setBlockMetadataWithNotify(x, y, z, (MathHelper.floor_double((entity.rotationYaw * 4 / 360) + 0.5) & 3), 2);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEntityLiquipackWorkbench){
            ItemStack stack = ((TileEntityLiquipackWorkbench) te).getStackInSlot(0);
            if(stack != null) {
                EntityItem drop = new EntityItem(world, x, y, z, ((TileEntityLiquipackWorkbench) te).getStackInSlot(0));
                world.spawnEntityInWorld(drop);
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
