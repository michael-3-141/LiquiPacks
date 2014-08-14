package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.common.SFluidTank;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLiquipackBucket extends Item {

    public ItemLiquipackBucket() {
        setUnlocalizedName("liquipackBucket");
        setTextureName(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5));
        setCreativeTab(CreativeTabs.tabTools);
        setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            list.add("<Press SHIFT for info>");
        }
        else {
            list.add("Mode: " + StatCollector.translateToLocal("liquipackbucket.mode." + (isDrainingMode(stack) ? 1 : 0)));
            list.add("Tank: " + (getSelectedTank(stack)+1));
            list.add(EnumChatFormatting.ITALIC + "Shift right click with the");
            list.add(EnumChatFormatting.ITALIC + "item in your hand to switch modes/tanks");
        }

    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        //Change modes if player is shifting
        if(player.isSneaking()){
            FMLNetworkHandler.openGui(player, Liquislots.INSTANCE, 2, player.worldObj, 0, 0, 0);
            return stack;
        }

        //Get right click position
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, isDrainingMode(stack));
        FluidStack result = null;
        ItemStack liquipack = player.inventory.armorItemInSlot(2);
        LiquipackStack tanks = new LiquipackStack(liquipack);
        if(getSelectedTank(stack) >= tanks.getTanks().length){
            if(!world.isRemote){
                player.addChatComponentMessage(new ChatComponentText("You don't have any tank in slot " + (getSelectedTank(stack) + 1) + " of your liquipack."));
            }
            return stack;
        }
        SFluidTank tank = tanks.getTankForStack(getSelectedTank(stack));

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

                    if(result != null && tank.fill(result, false) == FluidContainerRegistry.BUCKET_VOLUME){
                        tank.fill(result, true);
                        tanks.setTankInStack(tank, getSelectedTank(stack));
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
                    //Try to place
                    if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {
                        this.tryPlaceContainedLiquid(world, x, y, z, stack, tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true));
                        tanks.setTankInStack(tank, getSelectedTank(stack));
                    }
                }
            }
        }
        return stack;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z, ItemStack stack, FluidStack fluidStack)
    {
        if (isDrainingMode(stack)) //Is empty
        {
            return false;
        }
        else
        {
            if(fluidStack.getFluid().getBlock() == null)return false;

            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.isHellWorld && fluidStack.getFluid() == FluidRegistry.WATER)
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

                    world.setBlock(x, y, z, fluidStack.getFluid() == FluidRegistry.WATER ? Blocks.flowing_water : fluidStack.getFluid() == FluidRegistry.LAVA ? Blocks.flowing_lava : fluidStack.getFluid().getBlock(), 0, 3);
                    world.markBlockForUpdate(x, y, z);
                }

                return true;
            }
        }
    }

    public static boolean isDrainingMode(ItemStack stack){
        NBTTagCompound compound = safeGetStackCompound(stack);
        return compound.getBoolean("isDrainingMode");
    }

    public static ItemStack setDrainingMode(ItemStack stack, boolean drainingMode){
        NBTTagCompound compound = safeGetStackCompound(stack);
        compound.setBoolean("isDrainingMode", drainingMode);
        stack.setTagCompound(compound);
        return stack;
    }

    public static int getSelectedTank(ItemStack stack){
        NBTTagCompound compound = safeGetStackCompound(stack);
        return compound.getInteger("tank");
    }

    public static ItemStack setSelectedTank(ItemStack stack, int tank){
        NBTTagCompound compound = safeGetStackCompound(stack);
        compound.setInteger("tank", tank);
        stack.setTagCompound(compound);
        return stack;
    }

    public static NBTTagCompound safeGetStackCompound(ItemStack stack){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null){
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        return compound;
    }
}
