package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Liquislots;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLiquipackWorkbench extends BlockContainer {

    public static final PropertyDirection PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected BlockLiquipackWorkbench() {
        super(Material.iron);
        setUnlocalizedName("liquipackWorkbench");
        setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_FACING, EnumFacing.NORTH));
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);

        GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));

        setResistance(7.0F);
        setHardness(2.0F);

    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, PROPERTY_FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PROPERTY_FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PROPERTY_FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PROPERTY_FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityLiquipackWorkbench();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            FMLNetworkHandler.openGui(player, Liquislots.INSTANCE, 1, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState block) {
        TileEntity te = world.getTileEntity(pos);
        if(te != null && te instanceof TileEntityLiquipackWorkbench){
            ItemStack stack = ((TileEntityLiquipackWorkbench) te).getStackInSlot(0);
            if(stack != null) {
                EntityItem drop = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((TileEntityLiquipackWorkbench) te).getStackInSlot(0));
                world.spawnEntityInWorld(drop);
            }
        }
        super.breakBlock(world, pos, block);
    }

    @Override
    public int getRenderType() {
        return 3;
    }
}
