package com.michael.e.liquislots.network.proxy;

import com.michael.e.liquislots.client.models.ModelLiquipack;

public class ClientProxy extends CommonProxy{
    ModelLiquipack packModel = new ModelLiquipack();

    public ModelLiquipack getModel(){
        return packModel;
    }
}
