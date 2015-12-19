package com.michael.e.liquislots.item;

import com.michael.e.liquislots.Liquislots;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemLiquipacksBase extends Item{

    public ItemLiquipacksBase(String name) {
        super();
        GameRegistry.registerItem(this, name);
        setUnlocalizedName(name);
        setCreativeTab(Liquislots.INSTANCE.tabLiquipacks);
    }
}
