package com.michael.e.liquislots.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;

public class MiscUtil {

    public static ByteBuf blockPosToBytes(BlockPos pos, ByteBuf buf){
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        return buf;
    }

    public static BlockPos bytesToBlockPos(ByteBuf buf){
        return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}
