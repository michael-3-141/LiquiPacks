package com.michael.e.liquislots.common.util;

import com.michael.e.liquislots.item.ILiquipackArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LiquipackStack {

    private ItemStack stack;

    public LiquipackStack(ItemStack stack){
        if(stack.getTagCompound() == null)stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("tanks"))stack.getTagCompound().setTag("tanks", new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("upgrades"))stack.getTagCompound().setTag("upgrades", new NBTTagCompound());
        this.stack = stack;
    }

    private NBTNumberedList getTankList(){
        return new NBTNumberedList(stack.getTagCompound().getCompoundTag("tanks"));
    }

    private NBTNumberedList getUpgradeList(){
        return new NBTNumberedList(stack.getTagCompound().getCompoundTag("upgrades"));
    }

    public LiquipackTank getTank(int tank)
    {
        return LiquipackTank.loadFromNBT(getTankList().get(tank));
    }

    public LiquipackTank[] getTanks(){
        LiquipackTank[] tanks = new LiquipackTank[4];
        for(int i = 0; i < tanks.length; i++){
            tanks[i] = LiquipackTank.loadFromNBT(getTankList().get(i));
        }
        return tanks;
    }

    public int getTankCount(){
        int i = 0;
        for(LiquipackTank tank : getTanks()){
            if(tank != null)i++;
        }
        return i;
    }

    public ItemStack setTank(LiquipackTank tank, int tankIndex)
    {
        if(tank != null) {
            getTankList().set(tankIndex, tank.writeToNBT(new NBTTagCompound()));
        }
        else{
            getTankList().remove(tankIndex);
        }
        return stack;
    }

    public ItemStack addTank(LiquipackTank tank)
    {
        if(tank == null)return stack;
        getTankList().add(tank.writeToNBT(new NBTTagCompound()));
        return stack;
    }

    public ItemStack setArmor(ItemStack protection){
        if(!(protection.getItem() instanceof ILiquipackArmor))throw new IllegalArgumentException("Liquipack armor item must implement ILiquipackArmor");
        NBTTagCompound compound = new NBTTagCompound();
        protection.writeToNBT(compound);
        stack.getTagCompound().setTag("armor", compound);
        return stack;
    }

    public ItemStack getArmor(){
        return ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("armor"));
    }

    public ItemStack removeArmor(){
        stack.getTagCompound().removeTag("armor");
        return stack;
    }

    public LiquipackUpgrade[] getUpgrades(){
        LiquipackUpgrade[] upgrades = new LiquipackUpgrade[getUpgradeCount()];
        for(int i = 0; i < upgrades.length; i++){
            upgrades[i] = LiquipackUpgrade.loadFromNBT(getUpgradeList().get(i));
        }
        return upgrades;
    }

    public LiquipackUpgrade getUpgrade(int upgradeSlot)
    {
        return LiquipackUpgrade.loadFromNBT(getUpgradeList().get(upgradeSlot));
    }

    public int getUpgradeCount(){
        return getUpgradeList().getLength();
    }

    public ItemStack setUpgrade(LiquipackUpgrade upgrade, int upgradeIndex)
    {
        if(upgrade != null) {
            getUpgradeList().set(upgradeIndex, upgrade.writeToNBT(new NBTTagCompound()));
        }
        else{
            getUpgradeList().remove(upgradeIndex);
        }
        return stack;
    }

    public ItemStack addUpgrade(LiquipackUpgrade upgrade)
    {
        if(upgrade == null)return stack;
        getUpgradeList().add(upgrade.writeToNBT(new NBTTagCompound()));
        return stack;
    }
}
