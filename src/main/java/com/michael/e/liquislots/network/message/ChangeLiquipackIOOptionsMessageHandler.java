package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.common.util.MiscUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeLiquipackIOOptionsMessageHandler implements IMessageHandler<ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquipackIOOptionsMessage message, MessageContext ctx) {
        IThreadListener listener;
        if(ctx.side.isServer()) {
             listener = ctx.getServerHandler().playerEntity.getServerForPlayer();
        }
        else {
            listener = Minecraft.getMinecraft();
        }
        listener.addScheduledTask(() -> {
            EntityPlayer player = ctx.side.isServer() ? ctx.getServerHandler().playerEntity : Minecraft.getMinecraft().thePlayer;
            TileEntity te = player.worldObj.getTileEntity(message.blockPos);
            if (te instanceof TileEntityLiquipackIO) {
                TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
                teLio.setTank(message.tank);
                teLio.setDrainingMode(message.isDrainMode);
                teLio.buffer.setFluid(!message.fluidId.equals("") ? new FluidStack(FluidRegistry.getFluid(message.fluidId), message.fluidAmount) : null);

                if (ctx.side.isServer()) {
                    ((TileEntityLiquipackIO) te).container.sendUpdateToPlayers(message.copy());
                }
            }
        });
        return null;
    }

    public static class ChangeLiquipackIOOptionsMessage implements IMessage{

        public ChangeLiquipackIOOptionsMessage(){

        }

        public ChangeLiquipackIOOptionsMessage(int tank, boolean isDrainMode, String fluidId, int fluidAmount, BlockPos pos) {
            this.tank = tank;
            this.isDrainMode = isDrainMode;
            this.fluidAmount = fluidAmount;
            this.blockPos = pos;
            this.fluidId = fluidId;
        }

        public ChangeLiquipackIOOptionsMessage(TileEntityLiquipackIO teLio){
            this(teLio.getTank(), teLio.isDrainingMode(), teLio.buffer.getFluidType() == null ? "" : teLio.buffer.getFluidType().getName(), teLio.buffer.getFluidAmount(), teLio.getPos());
        }

        public int tank;
        public boolean isDrainMode;
        public String fluidId;
        public int fluidAmount;
        public BlockPos blockPos;

        @Override
        public void fromBytes(ByteBuf buf) {
            tank = buf.readInt();
            isDrainMode = buf.readBoolean();
            fluidId = ByteBufUtils.readUTF8String(buf);
            fluidAmount = buf.readInt();
            blockPos = MiscUtil.bytesToBlockPos(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(tank);
            buf.writeBoolean(isDrainMode);
            ByteBufUtils.writeUTF8String(buf, fluidId);
            buf.writeInt(fluidAmount);
            MiscUtil.blockPosToBytes(blockPos, buf);
        }

        public ChangeLiquipackIOOptionsMessage copy(){
            return new ChangeLiquipackIOOptionsMessage(tank, isDrainMode, fluidId, fluidAmount, blockPos);
        }
    }
}
