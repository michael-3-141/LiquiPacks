package com.michael.e.liquislots.common;

import com.michael.e.liquislots.item.ItemTank;
import com.michael.e.liquislots.item.ItemsRef;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeLiquipack implements IRecipe {

    private ItemStack result;

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        int foundLiquipacks = 0;
        int foundTanks = 0;
        ItemStack foundLiquipack = null;
        ItemStack foundTank = null;
        for(int i = 0; i < crafting.getSizeInventory(); i++){
            ItemStack stack = crafting.getStackInSlot(i);
            if(stack != null){
                if(stack.getItem() == ItemsRef.liquipack){
                    foundLiquipacks++;
                    foundLiquipack = stack;
                }
                else if(stack.getItem() instanceof ItemTank){
                    foundTanks++;
                    foundTank = stack;
                }
            }
        }
        if(foundLiquipacks == 1 && foundTanks == 1){
            TankStack tankStack = new TankStack(foundLiquipack.copy());
            if(tankStack.getTanks().length >= 4)return false;
            result = tankStack.addTankTankToStack(ItemTank.getFluidTankFromStack(foundTank));
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return result.copy();
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }
}
