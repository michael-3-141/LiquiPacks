package com.michael.e.liquislots.config;

import com.michael.e.liquislots.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class LiquislotsGuiConfig extends GuiConfig {

    public LiquislotsGuiConfig(GuiScreen screen){
        super(screen,
                new ConfigElement(ConfigHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.MOD_ID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigHandler.configFile.toString()));
    }
}
