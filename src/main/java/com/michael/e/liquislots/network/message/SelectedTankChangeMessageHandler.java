package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SelectedTankChangeMessageHandler implements IMessageHandler<SelectedTankChangeMessageHandler.SelectedTankChangeMessage, IMessage> {

    @Override
    public IMessage onMessage(SelectedTankChangeMessage message, MessageContext ctx) {

        if(ctx.getServerHandler().playerEntity.openContainer instanceof ContainerPlayerTanks){
            ((ContainerPlayerTanks) ctx.getServerHandler().playerEntity.openContainer).selectedTank = message.newId;
            ((ContainerPlayerTanks) ctx.getServerHandler().playerEntity.openContainer).onInventoryChanged();
        }
        return null;
    }

    public static class SelectedTankChangeMessage implements IMessage{

        public int newId;

        public SelectedTankChangeMessage(int newId) {
            this.newId = newId;
        }

        public SelectedTankChangeMessage() {
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            newId = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(newId);
        }
    }
}
