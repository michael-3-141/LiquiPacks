package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.common.SFluidTank;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLiquipack extends ItemArmor implements ISpecialArmor{

    public static ArmorMaterial liquipackMaterial = EnumHelper.addArmorMaterial("liquipackMaterial", 100, new int[]{0,0,0,0}, 0);

    public ItemLiquipack() {
        super(liquipackMaterial, 0, 1);
        setUnlocalizedName("liquipack");
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
        setTextureName(Reference.MOD_ID + ":liquipack1");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        LiquipackStack liquipackStack = new LiquipackStack(stack);
        return liquipackStack.getProtection() != null && liquipackStack.getProtection().getItemDamage() != 0;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.MOD_ID + ":models/armor/liquipack.png";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debug) {
        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            info.add("<Press SHIFT for more info>");
            /*if(debug && stack.getTagCompound() != null){
                String[] nbt = stack.getTagCompound().toString().split("(?<=\\G.{80})");
                for(String line : nbt){
                    info.add(line);
                }
            }*/
        }
        else {
            LiquipackStack liquipackStack = new LiquipackStack(stack);
            SFluidTank[] tanks = liquipackStack.getTanks();
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
            ItemStack protection = liquipackStack.getProtection();
            if(protection != null){
                info.add("Armor damage: " + (protection.getMaxDamage() - protection.getItemDamage()) + "/" + protection.getMaxDamage());
            }
        }
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return Liquislots.proxy.getModel();
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        LiquipackStack stack = new LiquipackStack(armor);
        if(stack.getProtection() != null){
            return ((ILiquipackProtection)stack.getProtection().getItem()).getProtectionProps(stack.getProtection());
        }
        return null;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        LiquipackStack liquipackStack = new LiquipackStack(armor);
        if(liquipackStack.getProtection() == null)return 0;
        ArmorProperties protection = ((ILiquipackProtection)liquipackStack.getProtection().getItem()).getProtectionProps(armor);
        return (int)(protection.AbsorbRatio * 25D);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        LiquipackStack liquipackStack = new LiquipackStack(stack);
        ItemStack protectorStack = liquipackStack.getProtection();
        if(protectorStack != null) {
            protectorStack.damageItem(damage, entity);
            if(protectorStack.getItemDamage() < protectorStack.getMaxDamage()) {
                liquipackStack.setProtection(protectorStack);
            }
            else{
                liquipackStack.removeProtection();
            }
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        ItemStack protection = new LiquipackStack(stack).getProtection();
        return protection != null ? 1.0 - (protection.getItemDamageForDisplay() / protection.getItemDamage()) : 0;
    }
}
