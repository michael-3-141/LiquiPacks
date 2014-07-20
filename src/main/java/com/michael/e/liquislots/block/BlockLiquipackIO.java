package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.TankStack;
import com.michael.e.liquislots.item.ItemLiquipack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class BlockLiquipackIO extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon blockTexture;

    protected BlockLiquipackIO() {
        super(Material.iron);
        setBlockName("liquipackIO");
        setCreativeTab(CreativeTabs.tabTools);
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
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) entity;
            TileEntity tile = world.getTileEntity(x, y, z);
            if(player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemLiquipack && tile instanceof TileEntityLiquipackIO){
                ItemStack stack = player.inventory.armorItemInSlot(2);
                TankStack tank = new TankStack(stack);
                FluidTank fluidTank = tank.getTankForStack(0);
                int left = fluidTank.getFluid().amount - ((TileEntityLiquipackIO) tile).buffer.fill(fluidTank.getFluid(), true);
                fluidTank.setFluid(new FluidStack(fluidTank.getFluid().getFluid(), left));
                tank.setTankInStack(fluidTank, 0);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityLiquipackIO();
    }
}
