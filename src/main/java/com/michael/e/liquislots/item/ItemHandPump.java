package com.michael.e.liquislots.item;

import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemHandPump extends ItemLiquipacksBase {

    public static final int MODE_PICK_UP = 1;
    public static final int MODE_PLACE = 2;
    public static final int MODE_AUTOMATIC = 0;

    public ItemHandPump() {
        super("liquipackBucket");
        setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if (debug && ConfigHandler.debugMode)
            list.add(stack.getTagCompound() != null ? stack.getTagCompound().toString() : "Null");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.ITALIC + "<Press SHIFT for info>");
        } else {
            list.add(EnumChatFormatting.BLUE + "Mode: " + StatCollector.translateToLocal("liquipackbucket.mode." + (getMode(stack))));
            list.add(EnumChatFormatting.DARK_GREEN + "Tank: " + (getSelectedTank(stack) + 1));
            list.add(EnumChatFormatting.ITALIC + "Shift right click with the");
            list.add(EnumChatFormatting.ITALIC + "item in your hand to switch modes/tanks");
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        //Get right click position
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        FluidStack result = null;
        ItemStack liquipack = player.inventory.armorItemInSlot(2);
        if(liquipack == null || liquipack.getItem() != ItemsRef.liquipack) return stack;
        LiquipackStack tanks = new LiquipackStack(liquipack);
        LiquipackTank tank = tanks.getTank(getSelectedTank(stack));
        if(tank == null){
            if(!world.isRemote){
                player.addChatComponentMessage(new ChatComponentText("You don't have any tank in slot " + (getSelectedTank(stack) + 1) + " of your liquipack."));
            }
            return stack;
        }

        //Special Behavior if Sneaking
        if(player.isSneaking()){
            //Try to drain into tank
            if(movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                TileEntity te = world.getTileEntity(movingobjectposition.getBlockPos());
                if(te instanceof IFluidHandler){
                    IFluidHandler handler = (IFluidHandler) te;
                    if(handler.canFill(movingobjectposition.sideHit, tank.getFluidType())){
                        int filled = handler.fill(movingobjectposition.sideHit, tank.getFluid(), true);
                        if(tank.getFluidAmount() == filled){
                            tank.setFluid(null);
                        }
                        else{
                            tank.setFluidAmount(tank.getFluidAmount() - filled);
                        }
                    }
                    tanks.setTank(tank, getSelectedTank(stack));
                    return stack;
                }
            }

            //Open Config GUI
            if (world.isRemote) {
                //Minecraft.getMinecraft().displayGuiScreen(new GuiHandPump(player, stack));
            }
            return stack;
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                //Store coords in local var for easy access
                BlockPos coords = movingobjectposition.getBlockPos();
                Block block = world.getBlockState(coords).getBlock();

                //Check if canMineBlock
                if (!world.canMineBlockBody(player, coords)) {
                    return stack;
                }

                boolean shouldPickUp = getMode(stack) == MODE_PICK_UP || (getMode(stack) == MODE_AUTOMATIC && (block instanceof IFluidBlock || block.getMaterial() == Material.lava || block.getMaterial() == Material.water));

                //Drain/Fill mode
                if (shouldPickUp) {
                    //Check if canPlayerEdit
                    if (!player.canPlayerEdit(coords, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    //Check if mod fluid found
                    if(block instanceof IFluidBlock){
                        result = new FluidStack(((IFluidBlock) block).getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
                    }
                    else{
                        //If its a vannila fluid, handle it
                        Material material = block.getMaterial();
                        IBlockState state = world.getBlockState(coords);

                        if (material == Material.water && (Integer)state.getValue(BlockLiquid.LEVEL) == 0) {
                            result = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
                        }

                        if (material == Material.lava && (Integer)state.getValue(BlockLiquid.LEVEL) == 0) {
                            result = new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
                        }
                    }

                    if(result != null && tank.fill(result, false) == FluidContainerRegistry.BUCKET_VOLUME){
                        tank.fill(result, true);
                        tanks.setTank(tank, getSelectedTank(stack));
                        world.setBlockToAir(coords);
                    }
                } else {
                    //Place handling
                    //Get the position for placement
                    BlockPos placeCoords = coords.offset(movingobjectposition.sideHit);

                    //Verify canPlayerEdit
                    if (!player.canPlayerEdit(placeCoords, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }
                    //Try to place
                    if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).getFluid().getBlock() != null) {
                        this.tryPlaceContainedLiquid(world, coords, stack, tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true));
                        tanks.setTank(tank, getSelectedTank(stack));
                    }
                }
            }
        }
        return stack;
    }

    /**
     * Attempts to place the liquid contained inside the bucket. (Modified version of the tryPlaceContainerLiquid in ItemBucket)
     */
    public boolean tryPlaceContainedLiquid(World world, BlockPos pos, ItemStack stack, FluidStack fluidStack)
    {
        if (getMode(stack) == MODE_PICK_UP)
        {
            return false;
        }
        else
        {
            if(fluidStack.getFluid().getBlock() == null)return false;

            Material material = world.getBlockState(pos).getBlock().getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(pos) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.doesWaterVaporize() && fluidStack.getFluid() == FluidRegistry.WATER)
                {
                    int i = pos.getX();
                    int j = pos.getY();
                    int k = pos.getZ();
                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
                else
                {
                    if (!world.isRemote && flag && !material.isLiquid())
                    {
                        world.destroyBlock(pos, true);
                    }

                    world.setBlockState(pos, fluidStack.getFluid() == FluidRegistry.WATER ? Blocks.flowing_water.getDefaultState()
                                           : fluidStack.getFluid() == FluidRegistry.LAVA ? Blocks.flowing_lava.getDefaultState()
                                           : fluidStack.getFluid().getBlock().getDefaultState(), 3);
                }

                return true;
            }
        }
    }

    public static int getMode(ItemStack stack){
        NBTTagCompound compound = safeGetStackCompound(stack);
        return compound.getInteger("mode");
    }

    public static ItemStack setMode(ItemStack stack, int mode){
        NBTTagCompound compound = safeGetStackCompound(stack);
        compound.setInteger("mode", mode);
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
