package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.TankStack;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public class ItemLiquipack extends ItemArmor{

    public static ArmorMaterial liquipackMaterial = EnumHelper.addArmorMaterial("liquipackMaterial", 100, new int[]{0,0,0,0}, 0);

    public IIcon[] icons = new IIcon[2];
    public static int[] tankCapacities = new int[]{10000, 20000};

    public ItemLiquipack() {
        super(liquipackMaterial, 0, 1);
        setUnlocalizedName("liquipack");
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        icons[0] = reg.registerIcon(Reference.MOD_ID + ":liquipack1");
        icons[1] = reg.registerIcon(Reference.MOD_ID + ":liquipack2");
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

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.MOD_ID + ":models/armor/liquipack.png";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean t) {
        TankStack tankStack = new TankStack(stack);
        for(FluidTank tank : tankStack.getTanks()) {
            if (tank.getFluid() == null) return;
            info.add("Contains: " + tank.getFluidAmount() + "x" + tank.getFluid().getFluid().getName());
        }
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return Liquislots.proxy.getModel();
    }

}
