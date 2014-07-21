package com.michael.e.liquislots.network;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class ChangeLiquipackIOOptionMessageHandler implements IMessageHandler<ChangeLiquipackIOOptionMessageHandler.ChangeLiquipackIOOptionMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquipackIOOptionMessage message, MessageContext ctx) {
        Container container = ctx.getServerHandler().playerEntity.openContainer;
        if(container instanceof ContainerLiquipackIO){
            TileEntityLiquipackIO te = ((ContainerLiquipackIO) container).getTe();
            te.setTank(message.tank);
            te.setDrainingMode(message.isDrainMode);
        }
        return null;
    }

    public static class ChangeLiquipackIOOptionMessage implements IMessage{

        public ChangeLiquipackIOOptionMessage(){

        }

        public ChangeLiquipackIOOptionMessage(int tank, boolean isDrainMode) {
            this.tank = tank;
            this.isDrainMode = isDrainMode;
        }

        public int tank;
        public boolean isDrainMode;

        @Override
        public void fromBytes(ByteBuf buf) {
            tank = buf.readInt();
            isDrainMode = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(tank);
            buf.writeBoolean(isDrainMode);
        }
    }
}
