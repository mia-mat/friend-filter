package ws.miaw.friendfilter;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendFilterCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "friendfilter";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("ff");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!ServerUtil.isOnHypixel()) {
            sendErrorMessage("You must be connected to Hypixel to execute that command.");
            return;
        }

        FriendConfiguration config = FriendFilterMod.getConfig();

        if (args.length < 1) {
            sendMessage("Current default behaviour is to " +
                    ((config.isDefaultShow()) ? ChatFormatting.GREEN + "show" : ChatFormatting.RED + "hide") +
                    ChatFormatting.WHITE + " join/leave messages and " +
                    (config.isConsideringGuild() ? ChatFormatting.GREEN + "consider" : ChatFormatting.RED + "not consider") + " guild messages"
            );

            sendMessage(ChatFormatting.GRAY + "Use '/ff help' for more information.");
            return;
        }

        if (args[0].equalsIgnoreCase("switch") || args[0].equalsIgnoreCase("toggle")) {
            config.setDefaultShow(!config.isDefaultShow());
            sendMessage("Set default behaviour to " +
                    ((config.isDefaultShow()) ? ChatFormatting.GREEN + "show" : ChatFormatting.RED + "hide") +
                    ChatFormatting.WHITE + " join and leave messages.");
            return;
        }

        if (args[0].equalsIgnoreCase("guild")) {
            config.setConsideringGuild(!config.isConsideringGuild());
            sendMessage((config.isConsideringGuild() ? ChatFormatting.GREEN + "Now" : ChatFormatting.RED + "No longer") +
                    " considering guild messages.");
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (config.getList().isEmpty()) {
                sendMessage("There are currently no players on the filter list. Use /ff add <username> to add some.");
                return;
            }

            StringBuilder message = new StringBuilder("Your filter list: \n");
            config.getList().stream().sorted(Comparator.naturalOrder()).forEachOrdered(username -> {
                message.append(ChatFormatting.LIGHT_PURPLE)
                        .append(" - ")
                        .append(ChatFormatting.GRAY)
                        .append(username).append("\n");
            });

            sendMessage(message.toString().trim());
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 2) {
                sendErrorMessage("Usage: /ff add <username>");
                return;
            }

            if (!args[1].matches("^\\w{1,16}$")) { // username regex (some OG names are <3 chars so min. 1)
                sendErrorMessage("Invalid username.");
                return;
            }

            if (config.addUsername(args[1].toLowerCase())) {
                sendMessage("Added " +
                        ChatFormatting.LIGHT_PURPLE + args[1].toLowerCase() +
                        ChatFormatting.WHITE + " to your filter list.");
            } else {
                sendErrorMessage(args[1].toLowerCase() + " is already on your filter list.");
            }

            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                sendErrorMessage("Usage: /ff remove <username>");
                return;
            }

            if (config.removeUsername(args[1].toLowerCase())) {
                sendMessage("Removed " +
                        ChatFormatting.LIGHT_PURPLE + args[1].toLowerCase() +
                        ChatFormatting.WHITE + " from your filter list.");
            } else {
                sendErrorMessage(args[1].toLowerCase() + " is not on your filter list.");
            }

            return;
        }

        if (args[0].equalsIgnoreCase("clear")) {
            config.clear();
            sendMessage("Cleared your filter list.");
            return;
        }

        // help
        sendMessage("/ff" + ChatFormatting.GRAY + " - shows default behaviours");
        sendMessage("/ff switch " + ChatFormatting.GRAY + "- toggles whether messages are shown or hidden by default");
        sendMessage("/ff guild " + ChatFormatting.GRAY + "- toggles listening for players in your guild");
        sendMessage("/ff list " + ChatFormatting.GRAY + "- view your filter list");
        sendMessage("/ff add <username> " + ChatFormatting.GRAY + "- adds a username to your filter list (to do the opposite of the default behaviour)");
        sendMessage("/ff remove <username> " + ChatFormatting.GRAY + "- removes a username from your filter list");
        sendMessage("/ff clear " + ChatFormatting.GRAY + "- clears your filter list");
        sendMessage("/ff help " + ChatFormatting.GRAY + "- shows this help message");

    }

    private void sendMessage(final String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.DARK_PURPLE + "[FF] " + ChatFormatting.WHITE + msg));
    }

    private void sendErrorMessage(final String msg) {
        sendMessage(ChatFormatting.RED + msg);
    }


}
