package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.SFluidTank;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;


public class ItemTank extends Item {

    private IIcon[] icons = new IIcon[3];

    public ItemTank() {
        setCreativeTab(CreativeTabs.tabMaterials);
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
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < icons.length; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IIcon getIconFromDamage(int dmg) {
        return icons[dmg];
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    public static int[] tankCapacities = new int[]{10000, 20000, 30000};

    public static SFluidTank getFluidTankFromStack(ItemStack stack) {
        return new SFluidTank(tankCapacities[stack.getItemDamage()]);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debug) {
        info.add("Tank capacity: " + tankCapacities[stack.getItemDamage()] + "mb");
    }
}
