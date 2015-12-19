package com.michael.e.liquislots.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeybindHandler {

    public static KeyBinding liquipackInventoryKey;

    //public static KeyBinding liquipackJetpackToggleKey;

    public static void init(){
        liquipackInventoryKey = new KeyBinding("key.liquipackinv", Keyboard.KEY_L, "key.categories.inventory");
        ClientRegistry.registerKeyBinding(liquipackInventoryKey);

        /*liquipackJetpackToggleKey = new KeyBinding("key.liquipackJetpackToggle", Keyboard.KEY_J, "key.categories.movement");
        ClientRegistry.registerKeyBinding(liquipackJetpackToggleKey);*/
    }
}
