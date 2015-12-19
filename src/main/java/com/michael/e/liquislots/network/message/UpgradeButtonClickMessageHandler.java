package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.item.ItemLiquipack;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpgradeButtonClickMessageHandler implements IMessageHandler<UpgradeButtonClickMessageHandler.UpgradeButtonClickMessage, IMessage> {

    @Override
    public IMessage onMessage(UpgradeButtonClickMessage message, MessageContext ctx) {
        WorldServer ws = ctx.getServerHandler().playerEntity.getServerForPlayer();
        ws.addScheduledTask(() -> {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof ContainerPlayerTanks) {
                ItemStack stack = player.inventory.armorItemInSlot(2);
                if (stack.getItem() instanceof ItemLiquipack) {
                    new LiquipackStack(stack).getUpgrade(message.upgradeIndex).getType().onClicked(player, player.worldObj, message.upgradeIndex);
                }
            }
        });
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
