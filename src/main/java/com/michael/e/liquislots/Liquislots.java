package com.michael.e.liquislots;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.client.LiquislotClientEventHandler;
import com.michael.e.liquislots.common.GuiHandler;
import com.michael.e.liquislots.config.ConfigHander;
import com.michael.e.liquislots.item.ItemsRef;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionMessageHandler;
import com.michael.e.liquislots.network.message.KeyPressMessageHandler;
import com.michael.e.liquislots.server.LiquislotServerEventHandler;
import com.michael.e.liquislots.network.message.SelectedTankChangeMessageHandler;
import com.michael.e.liquislots.network.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
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
        ConfigHander.init(e.getSuggestedConfigurationFile());

        ItemsRef.init();
        BlocksRef.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this.INSTANCE, new GuiHandler());

        netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        netHandler.registerMessage(KeyPressMessageHandler.class, KeyPressMessageHandler.KeyPressMessage.class, 0, Side.SERVER);
        netHandler.registerMessage(SelectedTankChangeMessageHandler.class, SelectedTankChangeMessageHandler.SelectedTankChangeMessage.class, 1, Side.SERVER);
        netHandler.registerMessage(ChangeLiquipackIOOptionMessageHandler.class, ChangeLiquipackIOOptionMessageHandler.ChangeLiquipackIOOptionMessage.class, 2, Side.SERVER);

        FMLCommonHandler.instance().bus().register(new LiquislotClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new LiquislotServerEventHandler());
        logger = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        GameRegistry.addRecipe(new ItemStack(ItemsRef.smallTank),
                "iii",
                "igi",
                "iii",

                'i',new ItemStack(Items.iron_ingot),
                'g',new ItemStack(Blocks.glass)
        );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipack, 1 , 0),
                "iii",
                " i ",
                "tit",

                'i', new ItemStack(Items.iron_ingot),
                't', new ItemStack(ItemsRef.smallTank)
            );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipack, 1 , 1),
                "iii",
                " i ",
                "tlt",

                'i', new ItemStack(Items.iron_ingot),
                't', new ItemStack(ItemsRef.smallTank),
                'l', new ItemStack(ItemsRef.liquipack, 1, 0)
                );

        proxy.initRenderers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {

    }

}
