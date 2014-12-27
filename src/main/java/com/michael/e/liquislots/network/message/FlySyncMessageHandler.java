package com.michael.e.liquislots.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class FlySyncMessageHandler implements IMessageHandler<FlySyncMessageHandler.FlySyncMessage, IMessage> {

    public static Map<EntityPlayer, Boolean> flyKeyDown = new HashMap<EntityPlayer, Boolean>();

    @Override
    public IMessage onMessage(FlySyncMessage message, MessageContext ctx) {
        flyKeyDown.put(ctx.getServerHandler().playerEntity, message.isFlyKeyPressed);
        return null;
    }

    public static class FlySyncMessage implements IMessage{

        public FlySyncMessage() {
        }

        public FlySyncMessage(boolean isFlyKeyPressed) {
            this.isFlyKeyPressed = isFlyKeyPressed;
        }

        public boolean isFlyKeyPressed;

        @Override
        public void fromBytes(ByteBuf buf) {
            isFlyKeyPressed = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBoolean(isFlyKeyPressed);
        }
    }
}
