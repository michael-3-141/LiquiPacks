package com.michael.e.liquislots.common.recipe;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.item.ItemsRef;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandler {

    public static void init(){
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.tank, 1, 0),
                "ggg",
                "gig",
                "ggg",

                'i', "blockIron",
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.tank, 1, 1),
                "gig",
                "gtg",
                "gig",

                'i', "blockIron",
                'g', "blockGlass",
                't', new ItemStack(ItemsRef.tank, 1, 0)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.tank, 1, 2),
                "gig",
                "dtd",
                "gig",

                'i', "blockIron",
                'g', "blockGlass",
                'd', "gemDiamond",
                't', new ItemStack(ItemsRef.tank, 1, 1)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipack, 1 , 0),
                "ili",
                "ibi",
                "lll",

                'i', "ingotIron",
                'l', new ItemStack(Items.leather),
                'b', "blockIron"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksRef.liquipackIO),
                "iri",
                "rtr",
                "iri",

                'i', "ingotIron",
                'r', "dustRedstone",
                't', new ItemStack(ItemsRef.tank, 1, 0)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.handPump),
                "rtr",
                " b ",

                'r', "dustRedstone",
                't', new ItemStack(ItemsRef.tank, 1, 0),
                'b', new ItemStack(Items.bucket)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorIron),
                "igi",
                "iii",
                "iii",

                'i', "ingotIron",
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorDiamond),
                "dgd",
                "ggg",
                "gdg",

                'd', "gemDiamond",
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksRef.liquipackWorkbench),
                "gig",
                "wcw",
                "igi",

                'g', "blockGlass",
                'i', "ingotIron",
                'w', "plankWood",
                'c', new ItemStack(Blocks.crafting_table)
        ));

        if(ItemsRef.upgradeLiquidXP != null) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.upgradeLiquidXP),
                    "iii",
                    "ibi",
                    "rrr",

                    'i', "ingotIron",
                    'b', new ItemStack(ItemsRef.handPump),
                    'r', "dustRedstone"
            ));
        }

        GameRegistry.addRecipe(new RecipeLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateTank());
    }
}
