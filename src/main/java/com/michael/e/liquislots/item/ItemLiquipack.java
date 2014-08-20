package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.common.SFluidTank;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
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
        return liquipackStack.getArmor() != null && liquipackStack.getArmor().getItemDamage() != 0;
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
            ItemStack protection = liquipackStack.getArmor();
            if(protection != null){
                info.add("Installed Armor: " + protection.getDisplayName() + " | Damage: " + (protection.getMaxDamage() - protection.getItemDamage()) + "/" + protection.getMaxDamage());
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
        if(stack.getArmor() != null){
            return ((ILiquipackArmor)stack.getArmor().getItem()).getProtectionProps(stack.getArmor());
        }
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        LiquipackStack liquipackStack = new LiquipackStack(armor);
        if(liquipackStack.getArmor() == null)return 0;
        ArmorProperties protection = ((ILiquipackArmor)liquipackStack.getArmor().getItem()).getProtectionProps(armor);
        return (int)(protection.AbsorbRatio * 25D);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        LiquipackStack liquipackStack = new LiquipackStack(stack);
        ItemStack protectorStack = liquipackStack.getArmor();
        if(protectorStack != null) {
            protectorStack.damageItem(damage, entity);
            if(protectorStack.getItemDamage() < protectorStack.getMaxDamage()) {
                liquipackStack.setArmor(protectorStack);
            }
            else{
                liquipackStack.removeArmor();
            }
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        ItemStack protection = new LiquipackStack(stack).getArmor();
        return protection != null ? 1.0 - (protection.getItemDamageForDisplay() / protection.getMaxDamage()) : 0;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if(!(entity instanceof EntityPlayer))return;
        EntityPlayer player = (EntityPlayer) entity;
        if(player.worldObj.isRemote || !(player.openContainer instanceof ContainerPlayerTanks))return;
        LiquipackStack tanks = new LiquipackStack(stack);
        int selectedTank = ((ContainerPlayerTanks)player.openContainer).selectedTank;
        ItemStack input = ItemStack.copyItemStack(tanks.getStackInSlot(0));
        ItemStack result = ItemStack.copyItemStack(tanks.getStackInSlot(1));
        SFluidTank tank = tanks.getTankForStack(selectedTank);
        boolean success = false;
        if(input == null)return;
        if(FluidContainerRegistry.isFilledContainer(input)){
            if(tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                if(!addStackToOutput(emptyFilledContainer(input), false, tanks))return;
                tank.fill(new FluidStack(FluidContainerRegistry.getFluidForFilledItem(input), FluidContainerRegistry.BUCKET_VOLUME), true);
                result = emptyFilledContainer(input);
                success = true;
            }
        }
        else if(FluidContainerRegistry.isEmptyContainer(input))
        {
            if(tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME){
                result = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
                tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                success = true;
            }

        }
        if(success) {
            tanks.decrStackSize(0, 1);
            if(result != null) {
                addStackToOutput(result, true, tanks);
            }
            tanks.setTankInStack(tank, selectedTank);
        }
    }

    private ItemStack emptyFilledContainer(ItemStack fullContainer){
        if(!FluidContainerRegistry.isFilledContainer(fullContainer))throw new IllegalArgumentException("Argument must be full container");
        if(fullContainer.getItem().hasContainerItem(fullContainer))return fullContainer.getItem().getContainerItem(fullContainer);
        for(FluidContainerRegistry.FluidContainerData containerData : FluidContainerRegistry.getRegisteredFluidContainerData()){
            if(containerData.filledContainer.isItemEqual(fullContainer)){
                if(containerData.emptyContainer.stackSize == 0){
                    containerData.emptyContainer.stackSize = 1;
                }
                return containerData.emptyContainer;
            }
        }
        return null;
    }

    private boolean addStackToOutput(ItemStack stack, boolean doPut, LiquipackStack tanks){
        ItemStack output = tanks.getStackInSlot(1);
        if(output == null){
            if(doPut){
                tanks.setInventorySlotContents(1, stack);
            }
            return true;
        }
        else if(stack.getItem() == output.getItem() && !(stack.getItem().getHasSubtypes() && stack.getItemDamage() != output.getItemDamage()) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()){
            if(doPut){
                tanks.incrStackSize(1, stack.stackSize > 0 ? stack.stackSize : 1);
            }
            return true;
        }
        else{
            return false;
        }
    }
}
