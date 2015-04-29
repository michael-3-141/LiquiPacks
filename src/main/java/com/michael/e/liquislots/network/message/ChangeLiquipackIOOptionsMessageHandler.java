package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ChangeLiquipackIOOptionsMessageHandler implements IMessageHandler<ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquipackIOOptionsMessage message, MessageContext ctx) {
        EntityPlayer player = ctx.side.isServer() ? ctx.getServerHandler().playerEntity : Minecraft.getMinecraft().thePlayer;
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if(te instanceof TileEntityLiquipackIO) {
            TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
            teLio.setTank(message.tank);
            teLio.setDrainingMode(message.isDrainMode);

            if(ctx.side.isServer()){
                Liquislots.INSTANCE.netHandler.sendToAllAround(message.copy(), new NetworkRegistry.TargetPoint(player.dimension, message.x, message.y, message.z, 10));
            }
        }
        return null;
    }

    public static class ChangeLiquipackIOOptionsMessage implements IMessage{

        public ChangeLiquipackIOOptionsMessage(){

        }

        public ChangeLiquipackIOOptionsMessage(int tank, boolean isDrainMode, int x, int y, int z) {
            this.tank = tank;
            this.isDrainMode = isDrainMode;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int tank;
        public boolean isDrainMode;
        public int x;
        public int y;
        public int z;

        @Override
        public void fromBytes(ByteBuf buf) {
            tank = buf.readInt();
            isDrainMode = buf.readBoolean();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(tank);
            buf.writeBoolean(isDrainMode);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        public ChangeLiquipackIOOptionsMessage copy(){
            return new ChangeLiquipackIOOptionsMessage(tank, isDrainMode, x, y, z);
        }
    }
}
