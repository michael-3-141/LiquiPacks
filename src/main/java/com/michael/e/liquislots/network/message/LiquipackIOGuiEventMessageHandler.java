package com.michael.e.liquislots.network.message;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.block.TileEntityLiquipackIO;
import com.michael.e.liquislots.client.gui.GuiLiquipackIO;
import com.michael.e.liquislots.common.util.MiscUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class LiquipackIOGuiEventMessageHandler implements IMessageHandler<LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage, LiquipackIOGuiEventMessageHandler.LiquipackIOGuiEventMessage> {

    @Override
    public LiquipackIOGuiEventMessage onMessage(LiquipackIOGuiEventMessage message, MessageContext ctx) {
        IThreadListener listener;
        if(ctx.side.isServer()) {
            listener = ctx.getServerHandler().playerEntity.getServerForPlayer();
        }
        else {
            listener = Minecraft.getMinecraft();
        }
        listener.addScheduledTask(() -> {
            //Switch based on side
            if (ctx.side == Side.SERVER) {
                //Get TileEntity
                TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);
                if (te instanceof TileEntityLiquipackIO) {
                    TileEntityLiquipackIO teLio = (TileEntityLiquipackIO) te;
                    if (message.opened) {
                        //Handle GUI Opening
                        if (ctx.getServerHandler().playerEntity.getDistanceSq(te.getPos()) < 60) {
                            //Add player to container, send him a te update and send him a message to open the gui
                            ((TileEntityLiquipackIO) te).container.addPlayer(ctx.getServerHandler().playerEntity);
                            Liquislots.INSTANCE.netHandler.sendTo(new ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage(teLio), ctx.getServerHandler().playerEntity);
                            Liquislots.INSTANCE.netHandler.sendTo(new LiquipackIOGuiEventMessage(te.getPos(), true),ctx.getServerHandler().playerEntity);
                        }
                    } else {
                        //Handle GUI Closing
                        //Remove player from container and send him a message to close the GUI
                        ((TileEntityLiquipackIO) te).container.removePlayer(ctx.getServerHandler().playerEntity);
                        Liquislots.INSTANCE.netHandler.sendTo(new LiquipackIOGuiEventMessage(te.getPos(), false),ctx.getServerHandler().playerEntity);
                    }
                }
            } else {
                if (message.opened) {
                    //If this is a open gui message, open the gui
                    Minecraft.getMinecraft().displayGuiScreen(new GuiLiquipackIO(Minecraft.getMinecraft().thePlayer, (TileEntityLiquipackIO) Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.pos)));
                } else {
                    //If this is a close gui message, close the gui
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
            }
        });
        return null;
    }

    public static class LiquipackIOGuiEventMessage implements IMessage {

        public LiquipackIOGuiEventMessage() {
        }

        public BlockPos pos;

        public boolean opened;

        public LiquipackIOGuiEventMessage(BlockPos pos, boolean opened) {
            this.pos = pos;
            this.opened = opened;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            pos = MiscUtil.bytesToBlockPos(buf);
            opened = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            MiscUtil.blockPosToBytes(pos, buf);
            buf.writeBoolean(opened);
        }
    }

}
