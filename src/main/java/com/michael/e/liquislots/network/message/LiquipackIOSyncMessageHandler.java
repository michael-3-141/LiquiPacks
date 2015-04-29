package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class LiquipackIOSyncMessageHandler implements IMessageHandler<LiquipackIOSyncMessageHandler.LiquipackIOSyncMessage, ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage>{

    @Override
    public ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage onMessage(LiquipackIOSyncMessage message, MessageContext ctx) {
        TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
        if(te instanceof TileEntityLiquipackIO) {
            TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
            return new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(teLio.getTank(), teLio.isDrainingMode(), teLio.xCoord, teLio.yCoord, teLio.zCoord);
        }
        return null;
    }

    public static class LiquipackIOSyncMessage implements IMessage{

        public LiquipackIOSyncMessage() {
        }

        public int x;
        public int y;
        public int z;

        public LiquipackIOSyncMessage(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {

            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }
    }

}
