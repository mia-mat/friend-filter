package me.mia.friendfilter;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FriendEventHandler {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent e) {
        // unformatted message doesn't give colour codes
        final String message = e.message.getUnformattedText();

        if (!(message.startsWith("Friend > ")
        || (FriendFilter.CONFIG.isConsideringGuild() && message.startsWith("Guild > ")))) return;

        // Unexpected, but don't deal with invalid messages nonetheless
        if (!(message.endsWith(" left.") || message.endsWith(" joined."))) return;

        final String[] messageComponents = message.split(" ");
        if (messageComponents.length != 4) return;

        final String friendName = messageComponents[2];

        if(!FriendFilter.CONFIG.shouldShowMessage(friendName)) {
            e.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            FriendFilter.setOnHypixel(false);
            return;
        }

        if (Minecraft.getMinecraft().isSingleplayer()) {
            FriendFilter.setOnHypixel(false);
            return;
        }

        final String ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if(ip.split("\\.").length == 2) {
            FriendFilter.setOnHypixel(ip.equalsIgnoreCase("hypixel.net"));
        } else {
            FriendFilter.setOnHypixel(ip.toLowerCase().endsWith(".hypixel.net"));
        }
    }

}
