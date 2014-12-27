package com.michael.e.liquislots.server;

public class LiquislotServerEventHandler {

    /*@SubscribeEvent
    public void entityConstruct(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && LiquipacksExtendedPlayer.get((EntityPlayer) event.entity) == null) {
            LiquipacksExtendedPlayer.register((EntityPlayer) event.entity);
        }
    }

   *@SubscribeEvent
    public void livingDeath(LivingDeathEvent event){
        if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
            NBTTagCompound compound = new NBTTagCompound();
            LiquipacksExtendedPlayer.get((EntityPlayer) event.entity).saveNBTData(compound);
            Liquislots.proxy.extendedEntityData.put(event.entity.getUniqueID().toString(), compound);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            NBTTagCompound playerData = Liquislots.proxy.extendedEntityData.get(event.entity.getUniqueID().toString());
            if (playerData != null) {
                event.entity.getExtendedProperties(LiquipacksExtendedPlayer.EXTENDED_PROPS_ID).loadNBTData(playerData);
            }
        }
    }*/
}
