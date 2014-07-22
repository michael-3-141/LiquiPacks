package com.michael.e.liquislots.client;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.item.ItemLiquipack;
import com.michael.e.liquislots.network.message.KeyPressMessageHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;

public class LiquislotClientEventHandler{

    @SubscribeEvent
    public void keyPressInput(InputEvent.KeyInputEvent e)
    {
        if(KeybindHandler.liquipackInventoryKey.isPressed() && Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2) != null && Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2).getItem() instanceof ItemLiquipack)
        {
            Liquislots.INSTANCE.netHandler.sendToServer(new KeyPressMessageHandler.KeyPressMessage('l'));
        }
        /*else if(Keyboard.isKeyDown(Keyboard.KEY_O))
        {
            Liquislots.INSTANCE.logger.info(TankExtendedPlayer.getPlayerTanks(Minecraft.getMinecraft().thePlayer).playerTank.getFluid().getFluid().getName());
        }*/
    }
}
