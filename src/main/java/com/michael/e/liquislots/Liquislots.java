package com.michael.e.liquislots;

import com.michael.e.liquislots.block.BlocksRef;
import com.michael.e.liquislots.client.LiquislotClientEventHandler;
import com.michael.e.liquislots.common.GuiHandler;
import com.michael.e.liquislots.common.recipe.RecipeLiquipack;
import com.michael.e.liquislots.common.recipe.RecipeUpdateLiquipack;
import com.michael.e.liquislots.common.recipe.RecipeUpdateTank;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.config.ConfigHandler;
import com.michael.e.liquislots.item.ItemTank;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
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
        netHandler.registerMessage(ChangeTankOptionsMessageHandler.class, ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage.class, 3, Side.SERVER);
        //netHandler.registerMessage(FlySyncMessageHandler.class, FlySyncMessageHandler.FlySyncMessage.class, 4, Side.SERVER);

        FMLCommonHandler.instance().bus().register(new LiquislotClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new LiquislotServerEventHandler());
        logger = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        ItemStack tank = new ItemStack(ItemsRef.tank, 1, 0);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "ggg",
                "gig",
                "ggg",

                'i',new ItemStack(Blocks.iron_block),
                'g',new ItemStack(Blocks.glass)
        );

        tank = new ItemStack(ItemsRef.tank, 1, 1);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "gig",
                "gtg",
                "gig",

                'i',new ItemStack(Blocks.iron_block),
                'g',new ItemStack(Blocks.glass),
                't', new ItemStack(ItemsRef.tank, 1, 0)
        );

        tank = new ItemStack(ItemsRef.tank, 1, 2);
        ItemTank.setTankForStack(tank, new LiquipackTank(ItemTank.getTankCapacities()[tank.getItemDamage()]));
        GameRegistry.addRecipe(tank,
                "gig",
                "dtd",
                "gig",

                'i',new ItemStack(Blocks.iron_block),
                'g',new ItemStack(Blocks.glass),
                'd',new ItemStack(Items.diamond),
                't', new ItemStack(ItemsRef.tank, 1, 0)
        );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipack, 1 , 0),
                "ili",
                "ibi",
                "lll",

                'i', new ItemStack(Items.iron_ingot),
                'l', new ItemStack(Items.leather),
                'b', new ItemStack(Blocks.iron_block)
            );

        GameRegistry.addRecipe(new ItemStack(BlocksRef.liquipackIO),
                "iri",
                "rtr",
                "iri",

                'i', new ItemStack(Items.iron_ingot),
                'r', new ItemStack(Items.redstone),
                't', new ItemStack(ItemsRef.tank, 1, 0)
                );

        GameRegistry.addRecipe(new ItemStack(ItemsRef.liquipackBucket),
                "rtr",
                " b ",

                'r', new ItemStack(Items.redstone),
                't', new ItemStack(ItemsRef.tank, 1, 0),
                'b', new ItemStack(Items.bucket)
                );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorIron),
                "igi",
                "iii",
                "iii",

                'i', new ItemStack(Items.iron_ingot),
                'g', "blockGlass"
                ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsRef.liquipackArmorDiamond),
                "dgd",
                "ggg",
                "gdg",

                'd', new ItemStack(Items.diamond),
                'g', "blockGlass"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksRef.liquipackWorkbench),
                "gig",
                "wcw",
                "igi",

                'g', "blockGlass",
                'i', new ItemStack(Items.iron_ingot),
                'w', "plankWood",
                'c', new ItemStack(Blocks.crafting_table)
        ));

        GameRegistry.addRecipe(new RecipeLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateLiquipack());
        GameRegistry.addRecipe(new RecipeUpdateTank());

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
