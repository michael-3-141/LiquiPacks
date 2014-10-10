package com.michael.e.liquislots.common.recipe;


import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemLiquipack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeUpdateLiquipack implements IRecipe{

    ItemStack result;

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int foundLiquipacks = 0;
        int foundItems = 0;
        ItemStack foundLiquipack = null;
        for(int i = 0; i < inventoryCrafting.getSizeInventory(); i++){
            ItemStack stack = inventoryCrafting.getStackInSlot(i);
            if(stack != null){
                foundItems++;
                if(stack.getItem() instanceof ItemLiquipack && ItemLiquipack.isOldFormat(stack)){
                    foundLiquipacks++;
                    foundLiquipack = stack;
                }
            }
        }
        if(foundLiquipacks == 1 && foundItems == 1){
            LiquipackTank[] tanks = new LiquipackTank[4];
            for(int i = 0; i < 4; i++){
                 tanks[i] = LiquipackTank.loadFromNBT(foundLiquipack.getTagCompound().getTagList("tanks", 10).getCompoundTagAt(i));
            }
            ItemStack resultStack = foundLiquipack.copy();
            resultStack.getTagCompound().removeTag("tanks");
            LiquipackStack stack = new LiquipackStack(resultStack);
            for(LiquipackTank tank : tanks){
                if(tank != null) {
                    stack.addTank(tank);
                }
            }
            result = resultStack;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
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
