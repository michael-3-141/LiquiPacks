package com.michael.e.liquislots.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class LiquipacksExtendedPlayer implements IExtendedEntityProperties{

    public static final String EXTENDED_PROPS_ID = "LIQUIPACKS";

    private boolean isJetpackActivated;

    public LiquipacksExtendedPlayer(EntityPlayer player) {
        this.isJetpackActivated = false;
    }

    public boolean isJetpackActivated() {
        return isJetpackActivated;
    }

    public void setJetpackActivated(boolean isJetpackActivated) {
        this.isJetpackActivated = isJetpackActivated;
    }

    public void toggleJetpackActivated(){
        isJetpackActivated = !isJetpackActivated;
    }

    @Override
    public void saveNBTData(NBTTagCompound player) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("isJetpackActivated", isJetpackActivated);
        player.setTag(EXTENDED_PROPS_ID, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        isJetpackActivated = compound.getCompoundTag(EXTENDED_PROPS_ID).getBoolean("isJetpackActivated");
    }

    @Override
    public void init(Entity entity, World world) {

    }

    public static void register(EntityPlayer player){
        player.registerExtendedProperties(EXTENDED_PROPS_ID, new LiquipacksExtendedPlayer(player));
    }

    public static LiquipacksExtendedPlayer get(EntityPlayer player){
        return (LiquipacksExtendedPlayer) player.getExtendedProperties(EXTENDED_PROPS_ID);
    }
}
