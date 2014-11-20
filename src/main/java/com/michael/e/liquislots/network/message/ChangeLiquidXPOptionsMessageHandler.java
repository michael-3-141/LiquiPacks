package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.container.ContainerLiquipackBucketOptions;
import com.michael.e.liquislots.common.upgrade.LiquidXPUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.common.util.LiquipackUpgrade;
import com.michael.e.liquislots.item.ItemLiquipack;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ChangeLiquidXPOptionsMessageHandler implements IMessageHandler<ChangeLiquidXPOptionsMessageHandler.ChangeLiquidXPOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquidXPOptionsMessage message, MessageContext ctx) {
        Container container = ctx.getServerHandler().playerEntity.openContainer;
        ItemStack stack = ctx.getServerHandler().playerEntity.getEquipmentInSlot(2);
        if(container instanceof ContainerLiquipackBucketOptions && stack.getItem() instanceof ItemLiquipack){
            LiquipackStack liquipack = new LiquipackStack(stack);
            int i = 0;
            for(LiquipackUpgrade upgrade : liquipack.getUpgrades()){
                if(LiquidXPUpgrade.isLiquidXPUpgrade(upgrade)){
                    LiquidXPUpgrade XPUpgrade = (LiquidXPUpgrade) upgrade;
                    XPUpgrade.setMode(message.mode);
                    XPUpgrade.setTank(message.tank);
                    liquipack.setUpgrade(XPUpgrade, i);
                    break;
                }
                i++;
            }
        }
        return null;
    }

    public static class ChangeLiquidXPOptionsMessage implements IMessage{

        public ChangeLiquidXPOptionsMessage(){

        }

        public ChangeLiquidXPOptionsMessage(int tank, int mode) {
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
