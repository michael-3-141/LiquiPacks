package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.item.ItemLiquipack;
import com.michael.e.liquislots.item.ItemsRef;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UpgradeButtonClickMessageHandler implements IMessageHandler<UpgradeButtonClickMessageHandler.UpgradeButtonClickMessage, IMessage> {

    @Override
    public IMessage onMessage(UpgradeButtonClickMessage message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if(player.openContainer instanceof ContainerPlayerTanks){
            ItemStack stack = player.inventory.armorItemInSlot(2);
            if(stack.getItem() instanceof ItemLiquipack) {
                new LiquipackStack(stack).getUpgrade(message.upgradeIndex).getType().onClicked(player, player.worldObj, message.upgradeIndex);
            }
        }
        return null;
    }

    public static class UpgradeButtonClickMessage implements IMessage{

        int upgradeIndex;

        public UpgradeButtonClickMessage(int upgradeIndex) {
            this.upgradeIndex = upgradeIndex;
        }

        public UpgradeButtonClickMessage() {
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            upgradeIndex = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(upgradeIndex);
        }
    }
}
