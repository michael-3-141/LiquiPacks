package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.item.ItemLiquipack;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyPressMessageHandler implements IMessageHandler<KeyPressMessageHandler.KeyPressMessage, IMessage> {

    @Override
    public IMessage onMessage(KeyPressMessage message, MessageContext ctx) {
        WorldServer ws = ctx.getServerHandler().playerEntity.getServerForPlayer();
        ws.addScheduledTask(() -> {
            if (message.key == 'l' && !ItemLiquipack.isOldFormat(ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2))) {
                FMLNetworkHandler.openGui(ctx.getServerHandler().playerEntity, Liquislots.INSTANCE, 0, ctx.getServerHandler().playerEntity.worldObj, 0, 0, 0);
            }
        });
        /*else if(message.key == 'j' && !ItemLiquipack.isOldFormat(ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2))){
            LiquipacksExtendedPlayer player = LiquipacksExtendedPlayer.get(ctx.getServerHandler().playerEntity);
            if(player != null){
                player.toggleJetpackActivated();
                ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("Liquipack Water Jetpack " + (player.isJetpackActivated() ? "On" : "Off")));
            }
        }*/
        return null;
    }

    public static class KeyPressMessage implements IMessage{

        public KeyPressMessage(){

        }

        public KeyPressMessage(char key)
        {
            this.key = key;
        }

        public char key;

        @Override
        public void fromBytes(ByteBuf buf) {
            key = buf.readChar();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeChar(key);
        }
    }
}
