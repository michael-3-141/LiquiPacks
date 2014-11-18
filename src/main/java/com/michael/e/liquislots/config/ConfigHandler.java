package com.michael.e.liquislots.config;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration configuration;
    public static File configFile;

    public static int smallTankCapacity;
    public static int mediumTankCapacity;
    public static int largeTankCapacity;

    public static int[] damageToCapacity;

    public static void init(File configFile){
        if(configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        if(ConfigHandler.configFile == null){
            ConfigHandler.configFile = configFile;
        }
    }

    public static void loadConfiguration(){
        smallTankCapacity = configuration.get(Configuration.CATEGORY_GENERAL, "smallTankCapacity", 8000).getInt();
        mediumTankCapacity = configuration.get(Configuration.CATEGORY_GENERAL, "mediumTankCapacity", 16000).getInt();
        largeTankCapacity = configuration.get(Configuration.CATEGORY_GENERAL, "largeTankCapacity", 32000).getInt();
        damageToCapacity = new int[]{ConfigHandler.smallTankCapacity, ConfigHandler.mediumTankCapacity, ConfigHandler.largeTankCapacity};

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}
