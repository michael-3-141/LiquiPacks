package com.michael.e.liquislots.network.proxy;

import com.michael.e.liquislots.client.models.ModelLiquipack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class CommonProxy{

    //Will be used for the jetpack upgrade
    public final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

    public ModelLiquipack getModel(){
        return null;
    }

    public void initRenderers(){

    }

    public void initKeybinds() {

    }

    public void reCreateModel() {

    }
}
