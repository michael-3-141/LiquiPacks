package com.michael.e.liquislots.common.recipe;

import com.michael.e.liquislots.common.LiquipackStack;
import com.michael.e.liquislots.item.ILiquipackArmor;
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
        int foundProtectors = 0;
        int foundOthers = 0;
        ItemStack foundLiquipack = null;
        ItemStack foundTank = null;
        ItemStack foundProtector = null;
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
                else if(stack.getItem() instanceof ILiquipackArmor){
                    foundProtectors++;
                    foundProtector = stack;
                }
                else{
                    foundOthers++;
                }
            }
        }
        if(foundLiquipacks == 1 && foundTanks == 1 && foundProtectors == 0 && foundOthers == 0){
            LiquipackStack liquipackStack = new LiquipackStack(foundLiquipack.copy());
            if(liquipackStack.getTanks().length >= 4)return false;
            result = liquipackStack.addTankTankToStack(ItemTank.getFluidTankFromStack(foundTank));
            return true;
        }
        if(foundLiquipacks == 1 && foundTanks == 0 && foundProtectors == 1 && foundOthers == 0){
            LiquipackStack liquipackStack = new LiquipackStack(foundLiquipack.copy());
            result = liquipackStack.setArmor(foundProtector);
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
