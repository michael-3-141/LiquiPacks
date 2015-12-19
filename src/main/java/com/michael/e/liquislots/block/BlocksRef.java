package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlocksRef {

    public static Block liquipackIO;
    public static Block liquipackWorkbench;

    public static void init(){

        liquipackIO = new BlockLiquipackIO();
        GameRegistry.registerTileEntity(TileEntityLiquipackIO.class, "te" + liquipackIO.getUnlocalizedName().substring(5));

        liquipackWorkbench = new BlockLiquipackWorkbench();
        GameRegistry.registerTileEntity(TileEntityLiquipackWorkbench.class, "te" + liquipackWorkbench.getUnlocalizedName().substring(5));
    }

    private static void registerBlockTextures(Block block){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void registerTextures(){
        registerBlockTextures(liquipackWorkbench);
        registerBlockTextures(liquipackIO);
    }
}
