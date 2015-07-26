package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.client.gui.GuiLiquipackIO;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public class LiquipackIOGuiEventMessageHandler implements IMessageHandler<LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage, LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage>{

    @Override
    public LiquipackIOGuiEventMessage onMessage(LiquipackIOGuiEventMessage message, MessageContext ctx) {
        //Switch based on side
        if(ctx.side == Side.SERVER) {
            //Get TileEntity
            TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
            if (te instanceof TileEntityLiquipackIO) {
                TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
                if(message.opened){
                    //Handle GUI Opening
                    if (ctx.getServerHandler().playerEntity.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) < 60) {
                        //Add player to container, send him a te update and send him a message to open the gui
                        ((TileEntityLiquipackIO) te).container.addPlayer(ctx.getServerHandler().playerEntity);
                        Liquislots.INSTANCE.netHandler.sendTo(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(teLio), ctx.getServerHandler().playerEntity);
                        return new LiquipackIOGuiEventMessage(te.xCoord, te.yCoord, te.zCoord, true);
                    }
                }
                else{
                    //Handle GUI Closing
                    //Remove player from container and send him a message to close the GUI
                    ((TileEntityLiquipackIO) te).container.removePlayer(ctx.getServerHandler().playerEntity);
                    return new LiquipackIOGuiEventMessage(te.xCoord, te.yCoord, te.zCoord, false);
                }
            }
        }
        else{
            if(message.opened){
                //If this is a open gui message, open the gui
                Minecraft.getMinecraft().displayGuiScreen(new GuiLiquipackIO(Minecraft.getMinecraft().thePlayer, (TileEntityLiquipackIO) Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x, message.y ,message.z)));
            }
            else{
                //If this is a close gui message, close the gui
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }
        return null;
    }

    public static class LiquipackIOGuiEventMessage implements IMessage{

        public LiquipackIOGuiEventMessage() {
        }

        public int x;
        public int y;
        public int z;

        public boolean opened;

        public LiquipackIOGuiEventMessage(int x, int y, int z, boolean opened) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.opened = opened;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            opened = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeBoolean(opened);
        }
    }

}
