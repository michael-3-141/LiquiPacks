package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.config.ConfigHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;


public class ItemTank extends ItemLiquipacksBase {

    private IIcon[] icons = new IIcon[3];

    public ItemTank() {
        super();
        setUnlocalizedName("tank");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for(int i = 0; i < icons.length; i++){
            icons[i] = register.registerIcon(Reference.MOD_ID + ":tank" + i);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        generateNbtForStack(stack);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < icons.length; i++){
            ItemStack stack = new ItemStack(item, 1, i);
            setTankForStack(stack, new LiquipackTank(getTankCapacities()[i]));
            list.add(stack);
        }
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        LiquipackTank tank = getTankForStack(stack);
        if(tank == null)return icons[0];
        for(int i = 0; i < getTankCapacities().length; i++){
            if(getTankCapacities()[i] == tank.getCapacity()){
                return icons[i];
            }
        }
        return icons[0];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return getIconIndex(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
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
            info.add("Tank capacity: " + getTankForStack(stack).getCapacity() + "mb");
            if (tank.getFluid() != null){
                info.add("Contains: " + tank.getFluidAmount() + "x" + tank.getFluid().getFluid().getLocalizedName(tank.getFluid()));
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
        return stack;
    }

}
