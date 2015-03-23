package com.michael.e.liquislots.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration configuration;
    public static File configFile;

    public static int smallTankCapacity;
    public static int mediumTankCapacity;
    public static int largeTankCapacity;

    public static boolean debugMode;

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
        debugMode = configuration.get(Configuration.CATEGORY_GENERAL, "enableDebugMode", false, "Enable this only if you know what you are doing").getBoolean();
        damageToCapacity = new int[]{ConfigHandler.smallTankCapacity, ConfigHandler.mediumTankCapacity, ConfigHandler.largeTankCapacity};

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}
