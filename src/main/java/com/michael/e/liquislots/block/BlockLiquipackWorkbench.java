package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Liquislots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLiquipackWorkbench extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon[] texture;

    protected BlockLiquipackWorkbench() {
        super(Material.iron);
        setBlockName("liquipackWorkbench");
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister register) {

    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return null;
    }*/

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityLiquipackWorkbench();
    }
}
