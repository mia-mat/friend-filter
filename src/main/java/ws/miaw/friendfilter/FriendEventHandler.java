package ws.miaw.friendfilter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FriendEventHandler {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent e) {
        if (!ServerUtil.isOnHypixel()) return;

        // These magic strings should ideally be in a separate class

        // unformatted message doesn't give colour codes
        final String message = e.message.getUnformattedText();

        if (!(message.startsWith("Friend > ")
                || (FriendFilterMod.getConfig().isConsideringGuild() && message.startsWith("Guild > ")))) return;

        if (!(message.endsWith(" left.") || message.endsWith(" joined."))) return;

        final String[] messageComponents = message.split(" ");
        if (messageComponents.length != 4) return;

        final String friendName = messageComponents[2];

        if (!FriendFilterMod.getConfig().shouldShowMessage(friendName)) {
            e.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            ServerUtil.setOnHypixel(false);
            return;
        }

        if (Minecraft.getMinecraft().isSingleplayer()) {
            ServerUtil.setOnHypixel(false);
            return;
        }

        final String ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if (ip.split("\\.").length == 2) {
            ServerUtil.setOnHypixel(ip.equalsIgnoreCase("hypixel.net"));
        } else {
            ServerUtil.setOnHypixel(ip.toLowerCase().endsWith(".hypixel.net"));
        }
    }

}
