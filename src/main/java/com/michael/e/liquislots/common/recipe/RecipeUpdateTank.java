package com.michael.e.liquislots.common.recipe;

import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemTank;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeUpdateTank implements IRecipe{

    ItemStack result;

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int foundTanks = 0;
        int foundItems = 0;
        ItemStack foundTank = null;
        for(int i = 0; i < inventoryCrafting.getSizeInventory(); i++){
            ItemStack stack = inventoryCrafting.getStackInSlot(i);
            if(stack != null){
                foundItems++;
                if(stack.getItem() instanceof ItemTank && ItemTank.getTankForStack(stack) == null){
                    foundTanks++;
                    foundTank = stack;
                }
            }
        }
        if(foundTanks == 1 && foundItems == 1 && foundTank.getItemDamage() <= 3) {
            result = foundTank.copy();
            ItemTank.setTankForStack(result, new LiquipackTank(ItemTank.getTankCapacities()[result.getItemDamage()]));
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
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
