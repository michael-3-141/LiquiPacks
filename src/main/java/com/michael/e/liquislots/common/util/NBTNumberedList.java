package com.michael.e.liquislots.common.util;

import net.minecraft.nbt.NBTTagCompound;

public class NBTNumberedList {

    private static final String SLOT = "Slot";
    private NBTTagCompound compound;

    public NBTNumberedList(NBTTagCompound compound) {
        this.compound = compound;
    }

    public NBTTagCompound get(int pos){
        return compound.hasKey(SLOT+pos) ? compound.getCompoundTag(SLOT+pos) : null;
    }

    public void set(int pos, NBTTagCompound compound){
        this.compound.setTag(SLOT+pos, compound);
    }

    public void add(NBTTagCompound compound){
        int i = 0;
        while(this.compound.hasKey(SLOT+i)){
            i++;
        }

        this.compound.setTag(SLOT+i, compound);
    }

    public void remove(int i){
        this.compound.removeTag(SLOT+i);
    }

    public NBTTagCompound getCompound() {
        return compound;
    }

    public int getLength(){
        int i = 0;
        while(compound.hasKey(SLOT+i)){
            i++;
        }
        return i;
    }
}