package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ChangeLiquipackIOOptionsMessageHandler implements IMessageHandler<ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquipackIOOptionsMessage message, MessageContext ctx) {
        EntityPlayer player = ctx.side.isServer() ? ctx.getServerHandler().playerEntity : Minecraft.getMinecraft().thePlayer;
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if(te instanceof TileEntityLiquipackIO) {
            TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
            teLio.setTank(message.tank);
            teLio.setDrainingMode(message.isDrainMode);
            teLio.buffer.setFluid(message.fluidId != -1 ? new FluidStack(FluidRegistry.getFluid(message.fluidId), message.fluidAmount) : null);

            if(ctx.side.isServer()){
                ((TileEntityLiquipackIO) te).container.sendUpdateToPlayers(message.copy());
            }
        }
        return null;
    }

    public static class ChangeLiquipackIOOptionsMessage implements IMessage{

        public ChangeLiquipackIOOptionsMessage(){

        }

        public ChangeLiquipackIOOptionsMessage(int tank, boolean isDrainMode, int fluidId, int fluidAmount, int x, int y, int z) {
            this.tank = tank;
            this.isDrainMode = isDrainMode;
            this.fluidId = fluidId;
            this.fluidAmount = fluidAmount;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public ChangeLiquipackIOOptionsMessage(TileEntityLiquipackIO teLio){
            this(teLio.getTank(), teLio.isDrainingMode(), teLio.buffer.getFluidType() == null ? -1 : teLio.buffer.getFluidType().getID(), teLio.buffer.getFluidAmount(), teLio.xCoord, teLio.yCoord, teLio.zCoord);
        }

        public int tank;
        public boolean isDrainMode;
        public int fluidId;
        public int fluidAmount;
        public int x;
        public int y;
        public int z;

        @Override
        public void fromBytes(ByteBuf buf) {
            tank = buf.readInt();
            isDrainMode = buf.readBoolean();
            fluidId = buf.readInt();
            fluidAmount = buf.readInt();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(tank);
            buf.writeBoolean(isDrainMode);
            buf.writeInt(fluidId);
            buf.writeInt(fluidAmount);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        public ChangeLiquipackIOOptionsMessage copy(){
            return new ChangeLiquipackIOOptionsMessage(tank, isDrainMode, fluidId, fluidAmount, x, y, z);
        }
    }
}
