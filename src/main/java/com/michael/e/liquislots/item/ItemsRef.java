package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemsRef {

    public static Item liquipack;
    public static Item tank;
    public static Item liquipackBucket;
    public static Item liquipackProtector;

    public static void init()
    {
        liquipack = new ItemLiquipack();
        regItem(liquipack);

        tank = new ItemTank();
        regItem(tank);

        liquipackBucket = new ItemLiquipackBucket();
        regItem(liquipackBucket);

        liquipackProtector = new ItemLiquipackProtector(6D / 25D, 90);
        regItem(liquipackProtector);
    }

    public static void regItem(Item item)
    {
        GameRegistry.registerItem(item, Reference.MOD_ID + "_" + item.getUnlocalizedName().substring(5));
    }
}
