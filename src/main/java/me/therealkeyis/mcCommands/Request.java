package me.therealkeyis.mcCommands;

import me.therealkeyis.DiscordBot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The /request command handler
 * 
 * This sends a message to the developer on discord requesting a new
 * feature or bugfix
 */
public class Request implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            DiscordBot.getInstance().messageDev(player.getDisplayName(), String.join(" ", args));
        }
        return true;
    }
}
