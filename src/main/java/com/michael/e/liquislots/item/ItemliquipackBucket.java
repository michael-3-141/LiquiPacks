package com.michael.e.liquislots.item;

import com.michael.e.liquislots.common.TankStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

public class ItemliquipackBucket extends Item {

    public ItemliquipackBucket() {
        setUnlocalizedName("liquipackBucket");
        setCreativeTab(CreativeTabs.tabTools);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        //Get right click position
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, isDrainingMode(stack));
        FluidStack result = null;

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                //Store coords in local vars for easy access
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;
                Block block = world.getBlock(x, y, z);

                //Check if canMineBlock
                if (!world.canMineBlock(player, x, y, z)) {
                    return stack;
                }

                //Drain/Fill mode
                if (isDrainingMode(stack)) {
                    //Check if canPlayerEdit
                    if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    //Check if mod fluid found
                    if(block instanceof IFluidBlock){
                        result = new FluidStack(((IFluidBlock) block).getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
                    }
                    else{
                        //If its a vannila fluid, handle it
                        Material material = block.getMaterial();
                        int meta = world.getBlockMetadata(x, y, z);

                        if (material == Material.water && meta == 0) {
                            result = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
                        }

                        if (material == Material.lava && meta == 0) {
                            result = new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
                        }
                    }

                    TankStack tanks = new TankStack(player.inventory.armorItemInSlot(2));
                    FluidTank tank = tanks.getTankForStack(0);//TODO: Replace with configured tank/auto detect
                    if(result != null && tank.fill(result, false) == FluidContainerRegistry.BUCKET_VOLUME){
                        tank.fill(result, true);
                        tanks.setTankInStack(tank, 0);//TODO: See above
                        world.setBlockToAir(x, y, z);
                    }
                } else {
                    //Place handling
                    //Get the position for placement
                    if (movingobjectposition.sideHit == 0) {
                        --y;
                    }

                    if (movingobjectposition.sideHit == 1) {
                        ++y;
                    }

                    if (movingobjectposition.sideHit == 2) {
                        --z;
                    }

                    if (movingobjectposition.sideHit == 3) {
                        ++z;
                    }

                    if (movingobjectposition.sideHit == 4) {
                        --x;
                    }

                    if (movingobjectposition.sideHit == 5) {
                        ++x;
                    }
                    //Verify canPlayerEdit
                    if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }
                    //Try to place - TODO: Add tank draining
                    this.tryPlaceContainedLiquid(world, x, y, z, stack);
                }
            }
        }
        return stack;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    /*public boolean tryPlaceContainedLiquid(World world, int x, int y, int z, ItemStack stack)
    {
        if (isDrainingMode(stack)) //Is empty
        {
            return false;
        }
        else
        {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.isHellWorld && this.isFull == Blocks.flowing_water)
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!world.isRemote && flag && !material.isLiquid())
                    {
                        world.func_147480_a(x, y, z, true);
                    }

                    world.setBlock(x, y, z, this.isFull, 0, 3);
                }

                return true;
            }
        }
    }*/

    public boolean isDrainingMode(ItemStack stack){
        return true;
    }
}
