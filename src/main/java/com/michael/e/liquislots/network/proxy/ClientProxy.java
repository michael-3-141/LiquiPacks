package com.michael.e.liquislots.network.proxy;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.client.KeybindHandler;
import com.michael.e.liquislots.client.models.ModelLiquipack;
import com.michael.e.liquislots.client.renderers.LiquipackIORenderer;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
    ModelLiquipack packModel = new ModelLiquipack();

    public ModelLiquipack getModel(){
        return packModel;
    }

    @Override
    public void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquipackIO.class, new LiquipackIORenderer());
    }

    @Override
    public void initKeybinds() {
        KeybindHandler.init();
    }

    @Override
    public void reCreateModel() {
        packModel = new ModelLiquipack();
    }
}
