package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.item.ItemHandPump;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ChangeTankOptionsMessageHandler implements IMessageHandler<ChangeTankOptionsMessageHandler.ChangeTankOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeTankOptionsMessage message, MessageContext ctx) {
        Container container = ctx.getServerHandler().playerEntity.openContainer;
        ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItem();
        if(container instanceof ContainerLiquipackBucketOptions && stack.getItem() instanceof ItemHandPump){
            ItemHandPump.setSelectedTank(stack, message.tank);
            ItemHandPump.setMode(stack, message.mode);
        }
        return null;
    }

    public static class ChangeTankOptionsMessage implements IMessage{

        public ChangeTankOptionsMessage(){

        }

        public ChangeTankOptionsMessage(int tank, int mode) {
            this.tank = tank;
            this.mode = mode;
        }

        public int tank;
        public int mode;

        @Override
        public void fromBytes(ByteBuf buf) {
            tank = buf.readInt();
            mode = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(tank);
            buf.writeInt(mode);
        }
    }
}
