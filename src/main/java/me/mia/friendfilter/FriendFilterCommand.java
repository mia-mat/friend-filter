package me.mia.friendfilter;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.*;

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
        return new ArrayList<String>() {{
            add("ff");
        }};
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!FriendFilter.isOnHypixel()) {
            return;
        }

        FriendConfiguration config = FriendFilter.CONFIG;

        if(args.length < 1) {
            sendMessage("Current default behaviour is to " +
                    ((config.isDefaultShow()) ? ChatFormatting.GREEN + "show" : ChatFormatting.RED + "hide") +
                    ChatFormatting.WHITE + " join and leave messages. " +
                    ChatFormatting.GRAY + "Use '/ff help' for more information.");
            return;
        }

        if(args[0].equalsIgnoreCase("switch")) {
            config.setDefaultShow(!config.isDefaultShow());
            sendMessage("Set default behaviour to " +
                    ((config.isDefaultShow()) ? ChatFormatting.GREEN + "show" : ChatFormatting.RED + "hide") +
                    ChatFormatting.WHITE + " join and leave messages.");
            return;
        }

        if(args[0].equalsIgnoreCase("list")) {
            if(config.getList().isEmpty()) {
                sendMessage("There are currently no players on the filter list. Use /ff add <username> to add some.");
                return;
            }

            // to sort alphabetically
            List<String> sortedUsernames = new ArrayList<String>();
            sortedUsernames.addAll(config.getList());
            Collections.sort(sortedUsernames);

            StringBuilder message = new StringBuilder("Your filter list: \n");

            for (String username : config.getList()) {
                message.append(ChatFormatting.LIGHT_PURPLE)
                        .append(" - ")
                        .append(ChatFormatting.GRAY)
                        .append(username).append("\n");
            }
            sendMessage(message.toString().trim());
            return;
        }

        if(args[0].equalsIgnoreCase("add")) {
            if(args.length < 2) {
                sendErrorMessage("Usage: /ff add <username>");
                return;
            }

            if(!args[1].matches("^\\w{3,16}$")) {
                sendErrorMessage("Invalid username.");
                return;
            }

            if(config.addUsername(args[1].toLowerCase())) {
                sendMessage("Added " +
                        ChatFormatting.LIGHT_PURPLE + args[1].toLowerCase() +
                        ChatFormatting.WHITE + " to your filter list.");
            } else {
                sendErrorMessage(args[1].toLowerCase() + " is already on your filter list.");
            }

            return;
        }

        if(args[0].equalsIgnoreCase("remove")) {
            if(args.length < 2) {
                sendErrorMessage("Usage: /ff remove <username>");
                return;
            }

            if(config.removeUsername(args[1].toLowerCase())) {
                sendMessage("Removed " +
                        ChatFormatting.LIGHT_PURPLE + args[1].toLowerCase() +
                        ChatFormatting.WHITE + " from your filter list.");
            } else {
                sendErrorMessage(args[1].toLowerCase() + " is not on your filter list.");
            }

            return;
        }

        if(args[0].equalsIgnoreCase("clear")) {
            config.clear();
            sendMessage("Cleared your filter list.");
            return;
        }

        // help
        sendMessage("/ff"  + ChatFormatting.GRAY + " - shows the default behaviour");
        sendMessage("/ff switch " + ChatFormatting.GRAY + "- toggles whether messages are shown or hidden by default");
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
