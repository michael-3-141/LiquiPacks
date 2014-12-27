package com.michael.e.liquislots.config;

import com.michael.e.liquislots.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

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
