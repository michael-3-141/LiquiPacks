package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.client.gui.GuiHandPump;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemHandPump extends ItemLiquipacksBase {

    public static final int MODE_PICK_UP = 1;
    public static final int MODE_PLACE = 2;
    public static final int MODE_AUTOMATIC = 0;
    private static final int[] sideHitToForgeDirection = {0,1,5,4,2,3};

    public ItemHandPump() {
        super();
        setUnlocalizedName("liquipackBucket");
        setTextureName(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5));
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
                TileEntity te = world.getTileEntity(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                if(te instanceof IFluidHandler){
                    IFluidHandler handler = (IFluidHandler) te;
                    if(handler.canFill(ForgeDirection.getOrientation(sideHitToForgeDirection[movingobjectposition.sideHit]), tank.getFluidType())){
                        int filled = handler.fill(ForgeDirection.getOrientation(sideHitToForgeDirection[movingobjectposition.sideHit]), tank.getFluid(), true);
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiHandPump(player, stack));
            }
            return stack;
        }

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

                boolean shouldPickUp = getMode(stack) == MODE_PICK_UP || (getMode(stack) == MODE_AUTOMATIC && (block instanceof IFluidBlock || block.getMaterial() == Material.lava || block.getMaterial() == Material.water));

                //Drain/Fill mode
                if (shouldPickUp) {
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
                        tanks.setTank(tank, getSelectedTank(stack));
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
                    if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).getFluid().getBlock() != null) {
                        this.tryPlaceContainedLiquid(world, x, y, z, stack, tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true));
                        tanks.setTank(tank, getSelectedTank(stack));
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
        if (getMode(stack) == MODE_PICK_UP) //Is empty
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
