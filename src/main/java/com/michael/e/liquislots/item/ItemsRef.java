package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;

public class ItemsRef {

    private static Side side;
    public static Item liquipack;
    public static Item tank;
    public static Item handPump;

    public static Item liquipackArmorIron;
    public static Item liquipackArmorDiamond;

    //public static Item upgradeJetpack;
    public static Item upgradeLiquidXP;

    public static void init()
    {
        liquipack = new ItemLiquipack();

        tank = new ItemTank();

        handPump = new ItemHandPump();

        liquipackArmorIron = new ItemLiquipackArmor("liquipackArmorIron", 6D / 25D, 240);

        liquipackArmorDiamond = new ItemLiquipackArmor("liquipackArmorDiamond", 8D / 25D, 528);

        //upgradeJetpack = new ItemJetpackUpgrade();
        //regItemTexture(upgradeJetpack);
        //if(Loader.isModLoaded("OpenBlocks")) {
        upgradeLiquidXP = new ItemLiquidXPUpgrade();
        //}
    }

    private static void regItemTexture(Item item, int meta)
    {
        regItemTexture(item, meta, item.getUnlocalizedName().substring(5));
    }

    private static void regItemTexture(Item item, int meta, String modelName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + modelName, "inventory"));
    }

    public static void registerTextures(){
        regItemTexture(handPump, 0);
        regItemTexture(upgradeLiquidXP, 0);
        for(int i=0; i<ItemTank.subitems.length; i++){
            String name = tank.getUnlocalizedName(new ItemStack(tank, 1, i)).substring(5);
            regItemTexture(tank, i, name);
            ModelBakery.addVariantName(tank, Reference.MOD_ID + ":" + name);
        }
        regItemTexture(liquipackArmorIron, 0);
        regItemTexture(liquipackArmorDiamond, 0);
        regItemTexture(liquipack, 0);
    }
}
