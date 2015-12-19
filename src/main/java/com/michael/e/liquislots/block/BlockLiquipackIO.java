package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.config.ConfigHandler;
import com.michael.e.liquislots.network.message.LiquipackIOGuiEventMessageHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLiquipackIO extends BlockContainer {

    public static final PropertyBool PROPERTY_PIPE_NORTH = PropertyBool.create("pipenorth");
    public static final PropertyBool PROPERTY_PIPE_WEST = PropertyBool.create("pipewest");
    public static final PropertyBool PROPERTY_PIPE_EAST = PropertyBool.create("pipeeast");
    public static final PropertyBool PROPERTY_PIPE_SOUTH = PropertyBool.create("pipesouth");

    protected BlockLiquipackIO() {
        super(Material.iron);
        setUnlocalizedName("liquipackIO");
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
        setDefaultState(this.getDefaultState()
                .withProperty(PROPERTY_PIPE_NORTH, false)
                .withProperty(PROPERTY_PIPE_WEST, false)
                .withProperty(PROPERTY_PIPE_EAST, false)
                .withProperty(PROPERTY_PIPE_SOUTH, false));

        GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        setHardness(2.0F);
        setResistance(10.0F);
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(PROPERTY_PIPE_NORTH, world.getTileEntity(pos.north()) instanceof IFluidHandler)
                .withProperty(PROPERTY_PIPE_WEST, world.getTileEntity(pos.west()) instanceof IFluidHandler)
                .withProperty(PROPERTY_PIPE_EAST, world.getTileEntity(pos.east()) instanceof IFluidHandler)
                .withProperty(PROPERTY_PIPE_SOUTH, world.getTileEntity(pos.south()) instanceof IFluidHandler);

    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, PROPERTY_PIPE_NORTH, PROPERTY_PIPE_WEST, PROPERTY_PIPE_EAST, PROPERTY_PIPE_SOUTH);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityLiquipackIO();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote){
            Liquislots.INSTANCE.netHandler.sendToServer(new LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage(pos, true));
        }

        if(!world.isRemote && player.isSneaking() && ConfigHandler.debugMode)
        {
            TileEntityLiquipackIO te = (TileEntityLiquipackIO) world.getTileEntity(pos);
            if(te.buffer.getFluid() != null && te.buffer.getFluidType() != null) {
                player.addChatComponentMessage(new ChatComponentText(te.buffer.getFluidType().getName() + " " + te.buffer.getFluid().amount));
            }
            player.addChatComponentMessage(new ChatComponentText(te.isDrainingMode() + " " + te.getTank()));
        }
        return true;
    }

    @Override
    public int getRenderType() {
        return 3;
    }
}
