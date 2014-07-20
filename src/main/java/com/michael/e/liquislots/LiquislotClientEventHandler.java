package com.michael.e.liquislots;

import com.michael.e.liquislots.item.ItemLiquipack;
import com.michael.e.liquislots.network.KeyPressMessageHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class LiquislotClientEventHandler{

    @SubscribeEvent
    public void keyPressInput(InputEvent.KeyInputEvent e)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_L) && Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2) != null && Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(38).getItem() instanceof ItemLiquipack)
        {
            Liquislots.INSTANCE.netHandler.sendToServer(new KeyPressMessageHandler.KeyPressMessage('l'));
        }
        /*else if(Keyboard.isKeyDown(Keyboard.KEY_O))
        {
            Liquislots.INSTANCE.logger.info(TankExtendedPlayer.getPlayerTanks(Minecraft.getMinecraft().thePlayer).playerTank.getFluid().getFluid().getName());
        }*/
    }
}
