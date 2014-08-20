/*
package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.common.SFluidTank;
import com.michael.e.liquislots.common.container.ContainerPlayerTanks;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

*/
/**
 * Created by Michael on 0019, 8, 19, 2014.
 *//*

public class TanksMessageHandler implements IMessageHandler<TanksMessageHandler.TanksMessage, IMessage> {

    @Override
    public IMessage onMessage(TanksMessage message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player.openContainer instanceof ContainerPlayerTanks){
            ((ContainerPlayerTanks) player.openContainer).recieveTanksMessage(message);
        }
        return null;
    }

    public static class TanksMessage implements IMessage{

        public TanksMessage(){
            tanks = new SFluidTank[4];
        }

        public TanksMessage(SFluidTank[] tanks){
            this.tanks = tanks;
        }

        SFluidTank[] tanks;

        @Override
        public void fromBytes(ByteBuf buf) {
            char[] list = new char[buf.readInt()];
            for(int i = 0; i < list.length; i++){
                list[i] = buf.readChar();
            }
            String stringNbt = String.valueOf(list);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            NBTTagList list = new NBTTagList();
            for(SFluidTank tank : tanks){
                list.appendTag(tank.writeToNBT(new NBTTagCompound()));
            }
            buf.writeInt(list.toString().length());
            for(char c : list.toString().toCharArray()){
                buf.writeChar(c);
            }
        }
    }

}
*/
