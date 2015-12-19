package com.michael.e.liquislots.item;

import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.config.ConfigHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;


public class ItemTank extends ItemLiquipacksBase {

    public static final String[] subitems = {"small","medium","large"};

    public ItemTank() {
        super("tank");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int dmg = stack.getItemDamage();
        return getUnlocalizedName() + "." + (dmg < subitems.length ? subitems[dmg] : subitems[0]);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < subitems.length; i++){
            ItemStack stack = new ItemStack(item, 1, i);
            setTankForStack(stack, new LiquipackTank(getTankCapacities()[i]));
            list.add(stack);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }



    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        generateNbtForStack(stack);
    }

    public static int[] getTankCapacities(){
        return ConfigHandler.damageToCapacity;
    }

    public static LiquipackTank getFluidTankFromStack(ItemStack stack) {
        return new LiquipackTank(getTankCapacities()[stack.getItemDamage()]);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debug) {
        LiquipackTank tank = getTankForStack(stack);
        if(tank != null) {
            info.add(EnumChatFormatting.BLUE + "Tank capacity: " + getTankForStack(stack).getCapacity() + "mb");
            if (tank.getFluid() != null){
                info.add(EnumChatFormatting.DARK_AQUA + "Contains: " + tank.getFluidAmount() + "x" + tank.getFluidType().getLocalizedName(tank.getFluid()));
            }
        }
    }

    private static boolean generateNbtForStack(ItemStack stack){
        if(stack.getItemDamage() >= 0 && stack.getItemDamage() < 3) {
            NBTTagCompound compound = new NBTTagCompound();
            LiquipackTank tank = new LiquipackTank(getTankCapacities()[stack.getItemDamage()]);
            compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
            stack.setTagCompound(compound);
            return true;
        }
        return false;
    }

    public static LiquipackTank getTankForStack(ItemStack stack){
        if(stack.getTagCompound() == null || !stack.getTagCompound().hasKey("tank")){
            if(!generateNbtForStack(stack))return null;
        }
        return LiquipackTank.loadFromNBT(stack.getTagCompound().getCompoundTag("tank"));
    }

    public static ItemStack setTankForStack(ItemStack stack, LiquipackTank tank){
        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        for(int i = 0; i < getTankCapacities().length; i++){
            if(getTankCapacities()[i] == tank.getCapacity()){
                stack.setItemDamage(i);
            }
        }
        return stack;
    }

}
