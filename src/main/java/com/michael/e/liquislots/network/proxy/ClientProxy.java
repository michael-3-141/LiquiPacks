package com.michael.e.liquislots.network.proxy;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.client.KeybindHandler;
import com.michael.e.liquislots.client.models.ModelLiquipack;
import com.michael.e.liquislots.item.ItemsRef;

public class ClientProxy extends CommonProxy{
    ModelLiquipack packModel = new ModelLiquipack();

    public ModelLiquipack getModel(){
        return packModel;
    }

    @Override
    public void initRenderers() {

    }

    @Override
    public void initKeybinds() {
        KeybindHandler.init();
    }

    @Override
    public void reCreateModel() {
        packModel = new ModelLiquipack();
    }

    @Override
    public void initModels() {
        ItemsRef.registerTextures();
        BlocksRef.registerTextures();
    }
}
