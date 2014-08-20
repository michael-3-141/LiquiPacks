package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemsRef {

    public static Item liquipack;
    public static Item tank;
    public static Item liquipackBucket;

    public static Item liquipackArmorIron;
    public static Item liquipackArmorDiamond;

    public static void init()
    {
        liquipack = new ItemLiquipack();
        regItem(liquipack);

        tank = new ItemTank();
        regItem(tank);

        liquipackBucket = new ItemLiquipackBucket();
        regItem(liquipackBucket);

        liquipackArmorIron = new ItemLiquipackArmor(6D / 25D, 240).setUnlocalizedName("liquipackArmorIron").setTextureName(Reference.MOD_ID + ":liquipackArmorIron");
        regItem(liquipackArmorIron);

        liquipackArmorDiamond = new ItemLiquipackArmor(8D / 25D, 528).setUnlocalizedName("liquipackArmorDiamond").setTextureName(Reference.MOD_ID + ":liquipackArmorDiamond");
        regItem(liquipackArmorDiamond);
    }

    public static void regItem(Item item)
    {
        GameRegistry.registerItem(item, Reference.MOD_ID + "_" + item.getUnlocalizedName().substring(5));
    }
}
