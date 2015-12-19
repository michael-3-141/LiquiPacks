package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackTank;
import com.michael.e.liquislots.item.ItemsRef;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerTanksFluidUpdateMessageHandler implements IMessageHandler<PlayerTanksFluidUpdateMessageHandler.PlayerTanksFluidUpdateMessage, IMessage> {

    @Override
    public IMessage onMessage(PlayerTanksFluidUpdateMessage message, MessageContext ctx) {
        WorldServer ws = ctx.getServerHandler().playerEntity.getServerForPlayer();
        ws.addScheduledTask(() -> {
            if(ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2).getItem() == ItemsRef.liquipack) {
                LiquipackStack tanks = new LiquipackStack(ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2));
                LiquipackTank tank = tanks.getTank(message.tankId);
                tank.setFluid(message.stack);
                tanks.setTank(tank, message.tankId);
            }
        });
        return null;
    }

    public static class PlayerTanksFluidUpdateMessage implements IMessage {

        public FluidStack stack;
        public int tankId;

        public PlayerTanksFluidUpdateMessage(){

        }

        public PlayerTanksFluidUpdateMessage(FluidStack stack, int tankId){
            this.stack = stack;
            this.tankId = tankId;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            if(buf.capacity() == 4){
                this.stack = null;
            }
            else{
                this.stack = FluidRegistry.getFluidStack(ByteBufUtils.readUTF8String(buf), buf.readInt());
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            if(stack == null || stack.getFluid() == null || stack.amount == 0) {
                buf.writeInt(Integer.MAX_VALUE);
            }
            else{
                ByteBufUtils.writeUTF8String(buf, stack.getFluid().getName());
                buf.writeInt(stack.amount);
            }
        }
    }
}
