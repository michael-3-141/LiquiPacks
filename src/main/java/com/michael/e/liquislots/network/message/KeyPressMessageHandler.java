package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.item.ItemLiquipack;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class KeyPressMessageHandler implements IMessageHandler<KeyPressMessageHandler.KeyPressMessage, IMessage> {

    @Override
    public IMessage onMessage(KeyPressMessage message, MessageContext ctx) {
        if(message.key == 'l' && !ItemLiquipack.isOldFormat(ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2)))
        {
            FMLNetworkHandler.openGui(ctx.getServerHandler().playerEntity, Liquislots.INSTANCE, 0, ctx.getServerHandler().playerEntity.worldObj, 0,0,0);
        }
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
