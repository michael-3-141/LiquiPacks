package com.michael.e.liquislots.common.recipe;

import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.item.*;
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
        int foundUpgrades = 0;
        int foundOthers = 0;
        ItemStack foundLiquipack = null;
        ItemStack foundTank = null;
        ItemStack foundProtector = null;
        ItemStack foundUpgrade = null;
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
                else if(stack.getItem() instanceof ILiquipackUpgrade){
                    foundUpgrades++;
                    foundUpgrade = stack;
                }
                else{
                    foundOthers++;
                }
            }
        }
        if(foundLiquipacks == 1 && !ItemLiquipack.isOldFormat(foundLiquipack)) {
            if (foundTanks == 1 && foundProtectors == 0 && foundUpgrades == 0 && foundOthers == 0) {
                LiquipackStack liquipackStack = new LiquipackStack(foundLiquipack.copy());
                if (liquipackStack.getTankCount() >= 4) return false;
                result = liquipackStack.addTank(ItemTank.getFluidTankFromStack(foundTank));
                return true;
            }
            if (foundTanks == 0 && foundProtectors == 1 && foundUpgrades == 0 && foundOthers == 0) {
                LiquipackStack liquipackStack = new LiquipackStack(foundLiquipack.copy());
                result = liquipackStack.setArmor(foundProtector);
                return true;
            }
            if (foundTanks == 0 && foundProtectors == 0 && foundUpgrades == 1 && foundOthers == 0) {
                LiquipackStack liquipackStack = new LiquipackStack(foundLiquipack.copy());
                if(liquipackStack.hasUpgrade(((ILiquipackUpgrade)foundUpgrade.getItem()).getUpgradeForStack(foundUpgrade).getType()))return false;
                result = liquipackStack.addUpgrade(((ILiquipackUpgrade)foundUpgrade.getItem()).getUpgradeForStack(foundUpgrade));
                return true;
            }
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

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[inv.getSizeInventory()];
    }
}
