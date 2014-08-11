package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.item.ItemLiquipackBucket;
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
        if(container instanceof ContainerLiquipackBucketOptions && stack.getItem() instanceof ItemLiquipackBucket){
            ItemLiquipackBucket.setSelectedTank(stack, message.tank);
            ItemLiquipackBucket.setDrainingMode(stack, message.isDrainMode);
        }
        return null;
    }

    public static class ChangeTankOptionsMessage implements IMessage{

        public ChangeTankOptionsMessage(){

        }

        public ChangeTankOptionsMessage(int tank, boolean isDrainMode) {
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
