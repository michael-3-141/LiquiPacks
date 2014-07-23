package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemsRef {

    public static Item liquipack;
    public static Item smallTank;
    public static Item liquipackBucket;

    public static void init()
    {
        liquipack = new ItemLiquipack();
        regItem(liquipack);

        smallTank = new Item().setUnlocalizedName("smallTank").setCreativeTab(CreativeTabs.tabMaterials);
        smallTank.setTextureName(Reference.MOD_ID + ":" + smallTank.getUnlocalizedName().substring(5));
        regItem(smallTank);

        liquipackBucket = new ItemliquipackBucket();
        regItem(liquipackBucket);
    }

    public static void regItem(Item item)
    {
        GameRegistry.registerItem(item, Reference.MOD_ID + "_" + item.getUnlocalizedName().substring(5));
    }
}
