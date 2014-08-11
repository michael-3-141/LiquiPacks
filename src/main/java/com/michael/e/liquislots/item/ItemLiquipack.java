package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.SFluidTank;
import com.michael.e.liquislots.common.TankStack;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLiquipack extends ItemArmor{

    public static ArmorMaterial liquipackMaterial = EnumHelper.addArmorMaterial("liquipackMaterial", 100, new int[]{0,0,0,0}, 0);

    public ItemLiquipack() {
        super(liquipackMaterial, 0, 1);
        setUnlocalizedName("liquipack");
        setCreativeTab(CreativeTabs.tabTools);
        setTextureName(Reference.MOD_ID + ":liquipack1");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
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
        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            info.add("<Press SHIFT for more info>");
        }
        else {
            TankStack tankStack = new TankStack(stack);
            SFluidTank[] tanks = tankStack.getTanks();
            if(tanks.length == 0){
                info.add("This item is useless without any tanks");
                info.add("Add tanks by putting them in a crafting");
                info.add("table with the liquipack");
            }
            int i = -1;
            for (SFluidTank tank : tanks) {
                i++;
                if (tank == null) return;
                String containsText = tank.getFluid() == null ? "Nothing" : tank.getFluidAmount() + "x" + tank.getFluid().getFluid().getLocalizedName(tank.getFluid());
                info.add("Tank " + (i + 1) + " | Capacity: " + tank.getCapacity() + "mb | Contains: " + containsText);
            }
        }
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return Liquislots.proxy.getModel();
    }

}
