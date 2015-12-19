package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.upgrade.LiquidXPUpgrade;
import com.michael.e.liquislots.common.upgrade.LiquipackUpgrade;
import com.michael.e.liquislots.common.util.LiquipackStack;
import com.michael.e.liquislots.item.ItemLiquipack;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeLiquidXPOptionsMessageHandler implements IMessageHandler<ChangeLiquidXPOptionsMessageHandler.ChangeLiquidXPOptionsMessage, IMessage> {

    @Override
    public IMessage onMessage(ChangeLiquidXPOptionsMessage message, MessageContext ctx) {
        WorldServer ws = ctx.getServerHandler().playerEntity.getServerForPlayer();
        ws.addScheduledTask(() -> {
            ItemStack stack = ctx.getServerHandler().playerEntity.inventory.armorItemInSlot(2);
            if(stack.getItem() instanceof ItemLiquipack){
                LiquipackStack liquipack = new LiquipackStack(stack);
                int i = 0;
                for(LiquipackUpgrade upgrade : liquipack.getUpgrades()){
                    if(LiquidXPUpgrade.isLiquidXPUpgrade(upgrade)){
                        LiquidXPUpgrade XPUpgrade = LiquidXPUpgrade.fromLiquipackUpgrade(upgrade);
                        XPUpgrade.setMode(message.mode);
                        XPUpgrade.setTank(message.tank);
                        liquipack.setUpgrade(XPUpgrade, i);
                        break;
                    }
                    i++;
                }
            }
        });
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
