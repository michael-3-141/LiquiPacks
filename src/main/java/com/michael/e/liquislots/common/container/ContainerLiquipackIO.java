package com.michael.e.liquislots.common.container;

import com.michael.e.liquislots.Liquislots;
import com.michael.e.liquislots.network.message.ChangeLiquipackIOOptionsMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

/**
 * Although this isn't really a container, I decided to call it one, because it has
 * essentially the same purpose: A server-side representation of GUIs open on the
 * client side. This container doesn't use the games container class because this GUI has no
 * slots, and that causes issues with the vannila container class.
 */
public class ContainerLiquipackIO {

    /**
     * The players that have the GUI open, equivalent to crafters in the vannila container.
     */
    private List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();

    public void addPlayer(EntityPlayerMP player){
        players.add(player);
    }

    public void removePlayer(EntityPlayerMP player){
        players.remove(player);
    }

    public void sendUpdateToPlayers(ChangeLiquipackIOOptionsMessageHandler.ChangeLiquipackIOOptionsMessage message){
        for(EntityPlayerMP player : players){
            Liquislots.INSTANCE.netHandler.sendTo(message, player);
        }
    }
}
