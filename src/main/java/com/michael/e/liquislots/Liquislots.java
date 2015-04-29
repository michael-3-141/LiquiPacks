package com.michael.e.liquislots;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.client.LiquislotClientEventHandler;
import com.michael.e.liquislots.common.GuiHandler;
import com.michael.e.liquislots.common.recipe.RecipeHandler;
import com.michael.e.liquislots.config.ConfigHandler;
import com.michael.e.liquislots.item.ItemsRef;
import com.michael.e.liquislots.network.message.*;
import com.michael.e.liquislots.network.proxy.CommonProxy;
import com.michael.e.liquislots.server.LiquislotServerEventHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = "com.michael.e.liquislots.config.GuiFactory")
public class Liquislots {

    @Mod.Instance
    public static Liquislots INSTANCE;

    @SidedProxy(modId = Reference.MOD_ID, serverSide = "com.michael.e.liquislots.network.proxy.CommonProxy", clientSide = "com.michael.e.liquislots.network.proxy.ClientProxy")
    public static CommonProxy proxy;

    public SimpleNetworkWrapper netHandler;
    public Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        ConfigHandler.init(e.getSuggestedConfigurationFile());

        ItemsRef.init();
        BlocksRef.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this.INSTANCE, new GuiHandler());

        netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        netHandler.registerMessage(KeyPressMessageHandler.class, KeyPressMessageHandler.KeyPressMessage.class, 0, Side.SERVER);
        netHandler.registerMessage(SelectedTankChangeMessageHandler.class, SelectedTankChangeMessageHandler.SelectedTankChangeMessage.class, 1, Side.SERVER);
        netHandler.registerMessage(ChangeLiquipackIOOptionsMessageHandler.class, ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage.class, 2, Side.SERVER);
        netHandler.registerMessage(ChangeLiquipackIOOptionsMessageHandler.class, ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage.class, 2, Side.CLIENT);
        netHandler.registerMessage(ChangeTankOptionsMessageHandler.class, ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage.class, 3, Side.SERVER);
        netHandler.registerMessage(ChangeLiquidXPOptionsMessageHandler.class, ChangeLiquidXPOptionsMessageHandler.ChangeLiquidXPOptionsMessage.class, 4, Side.SERVER);
        netHandler.registerMessage(UpgradeButtonClickMessageHandler.class, UpgradeButtonClickMessageHandler.UpgradeButtonClickMessage.class, 5, Side.SERVER);
        netHandler.registerMessage(LiquipackIOSyncMessageHandler.class, LiquipackIOSyncMessageHandler.LiquipackIOSyncMessage.class, 6, Side.SERVER);
        //netHandler.registerMessage(FlySyncMessageHandler.class, FlySyncMessageHandler.FlySyncMessage.class, 5, Side.SERVER);

        FMLCommonHandler.instance().bus().register(new LiquislotClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new LiquislotServerEventHandler());
        logger = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        RecipeHandler.init();
        proxy.initRenderers();
        proxy.initKeybinds();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {

    }

    public CreativeTabs tabLiquipacks = new CreativeTabs("tabLiquipacks") {
        @Override
        public Item getTabIconItem() {
            return ItemsRef.liquipack;
        }
    };

}
