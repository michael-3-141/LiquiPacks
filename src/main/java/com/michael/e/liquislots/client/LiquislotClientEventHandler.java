package com.michael.e.liquislots.client;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.Reference;
import com.michael.e.liquislots.config.ConfigHandler;
import com.michael.e.liquislots.item.ItemLiquipack;
import com.michael.e.liquislots.network.message.KeyPressMessageHandler;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;

public class LiquislotClientEventHandler{

    @SubscribeEvent
    public void onKeyPressInput(InputEvent.KeyInputEvent e)
    {
        if(KeybindHandler.liquipackInventoryKey.isPressed() && Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2) != null && Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2).getItem() instanceof ItemLiquipack)
        {
            Liquislots.INSTANCE.netHandler.sendToServer(new KeyPressMessageHandler.KeyPressMessage('l'));
        }
        /*else if(KeybindHandler.liquipackJetpackToggleKey.isPressed()){
            Liquislots.INSTANCE.netHandler.sendToServer(new KeyPressMessageHandler.KeyPressMessage('j'));
            LiquipacksExtendedPlayer player = LiquipacksExtendedPlayer.get(Minecraft.getMinecraft().thePlayer);
            if(player != null){
                player.toggleJetpackActivated();
            }
        }
        else if(Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()){
            Liquislots.INSTANCE.netHandler.sendToServer(new FlySyncMessageHandler.FlySyncMessage(true));
        }*/
    }

    private boolean lastFlyKeyState = false;

    /*@SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            boolean flyKeyState = Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed();
            if(flyKeyState != lastFlyKeyState){
                Liquislots.INSTANCE.netHandler.sendToServer(new FlySyncMessageHandler.FlySyncMessage(flyKeyState));
                FlySyncMessageHandler.flyKeyDown.put(Minecraft.getMinecraft().thePlayer, flyKeyState);
                lastFlyKeyState = flyKeyState;
            }
        }
    }*/

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equalsIgnoreCase(Reference.MOD_ID)){
            ConfigHandler.loadConfiguration();
        }
    }
}