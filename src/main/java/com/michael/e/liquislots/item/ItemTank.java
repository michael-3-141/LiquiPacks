package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.util.LiquipackTank;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

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
        return getUnlocalizedName() + "." + (getTankForStack(stack) != null ? getTankForStack(stack).getCapacity() : 0);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for(int i = 0; i < icons.length; i++){
            icons[i] = register.registerIcon(Reference.MOD_ID + ":tank" + i);
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < icons.length; i++){
            ItemStack stack = new ItemStack(item, 1);
            setTankForStack(stack, new LiquipackTank(tankCapacities[i]));
            list.add(stack);
        }
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        LiquipackTank tank = getTankForStack(stack);
        if(tank == null)return icons[0];
        for(int i = 0; i < tankCapacities.length; i++){
            if(tankCapacities[i] == tank.getCapacity()){
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

    public static int[] tankCapacities = new int[]{8000, 16000, 32000};

    public static LiquipackTank getFluidTankFromStack(ItemStack stack) {
        return new LiquipackTank(tankCapacities[stack.getItemDamage()]);
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

    public static LiquipackTank getTankForStack(ItemStack stack){
        if(stack.getTagCompound() == null || !stack.getTagCompound().hasKey("tank")){
            stack.setTagCompound(new NBTTagCompound());
            return null;
        }
        return LiquipackTank.loadFromNBT(stack.getTagCompound().getCompoundTag("tank"));
    }

    public static ItemStack setTankForStack(ItemStack stack, LiquipackTank tank){
        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        return stack;
    }
}
