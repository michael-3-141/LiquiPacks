package com.michael.e.liquislots.common.recipe;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemTank;
import com.michael.e.liquislots.item.ItemsRef;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {

    public static void init(){
        ItemStack tank = new ItemStack(ItemsRef.tank, 1, 0);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "ggg",
                "gig",
                "ggg",

                'i', new ItemStack(Blocks.iron_block),
                'g', new ItemStack(Blocks.glass)
        );

        tank = new ItemStack(ItemsRef.tank, 1, 1);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "gig",
                "gtg",
                "gig",

                'i',new ItemStack(Blocks.iron_block),
                'g',new ItemStack(Blocks.glass),
                't', new ItemStack(ItemsRef.tank, 1, 0)
        );

        tank = new ItemStack(ItemsRef.tank, 1, 2);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "gig",
                "dtd",
                "gig",

                'i',new ItemStack(Blocks.iron_block),
                'g',new ItemStack(Blocks.glass),
                'd',new ItemStack(Items.diamond),
                't', new ItemStack(ItemsRef.tank, 1, 0)
        );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipack, 1 , 0),
                "ili",
                "ibi",
                "lll",

                'i', new ItemStack(Items.iron_ingot),
                'l', new ItemStack(Items.leather),
                'b', new ItemStack(Blocks.iron_block)
        );

        GameRegistry.addRecipe(new ItemStack(BlocksRef.liquipackIO),
                "iri",
                "rtr",
                "iri",

                'i', new ItemStack(Items.iron_ingot),
                'r', new ItemStack(Items.redstone),
                't', new ItemStack(ItemsRef.tank, 1, 0)
        );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipackBucket),
                "rtr",
                " b ",

                'r', new ItemStack(Items.redstone),
                't', new ItemStack(ItemsRef.tank, 1, 0),
                'b', new ItemStack(Items.bucket)
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorIron),
                "igi",
                "iii",
                "iii",

                'i', new ItemStack(Items.iron_ingot),
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorDiamond),
                "dgd",
                "ggg",
                "gdg",

                'd', new ItemStack(Items.diamond),
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksRef.liquipackWorkbench),
                "gig",
                "wcw",
                "igi",

                'g', "blockGlass",
                'i', new ItemStack(Items.iron_ingot),
                'w', "plankWood",
                'c', new ItemStack(Blocks.crafting_table)
        ));

        GameRegistry.addRecipe(new RecipeLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateTank());
    }
}
