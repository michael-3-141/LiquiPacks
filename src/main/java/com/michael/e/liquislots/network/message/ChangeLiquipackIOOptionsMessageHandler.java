package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.container.ContainerLiquipackIO;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class ChangeLiquipackIOOptionsMessageHandler implements IMessageHandler<ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquipackIOOptionsMessage message, MessageContext ctx) {
        Container container = ctx.getServerHandler().playerEntity.openContainer;
        if(container instanceof ContainerLiquipackIO){
            TileEntityLiquipackIO te = ((ContainerLiquipackIO) container).getTe();
            te.setTank(message.tank);
            te.setDrainingMode(message.isDrainMode);
        }
        return null;
    }

    public static class ChangeLiquipackIOOptionsMessage implements IMessage{

        public ChangeLiquipackIOOptionsMessage(){

        }

        public ChangeLiquipackIOOptionsMessage(int tank, boolean isDrainMode) {
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
