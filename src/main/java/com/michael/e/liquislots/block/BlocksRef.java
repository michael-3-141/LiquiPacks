package com.michael.e.liquislots.block;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlocksRef {

    public static Block liquipackIO;
    public static Block liquipackWorkbench;

    public static void init(){

        liquipackIO = new BlockLiquipackIO();
        registerBlock(liquipackIO);
        GameRegistry.registerTileEntity(TileEntityLiquipackIO.class, "te" + liquipackIO.getUnlocalizedName());

        liquipackWorkbench = new BlockLiquipackWorkbench();
        registerBlock(liquipackWorkbench);
        GameRegistry.registerTileEntity(TileEntityLiquipackWorkbench.class, "te" + liquipackWorkbench.getUnlocalizedName());
    }

    private static void registerBlock(Block block){
        GameRegistry.registerBlock(block, Reference.MOD_ID + "_" + block.getUnlocalizedName().substring(5));
    }
}
