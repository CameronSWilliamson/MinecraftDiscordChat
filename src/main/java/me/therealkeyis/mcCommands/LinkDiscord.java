package me.therealkeyis.mcCommands;

import me.therealkeyis.Database;
import me.therealkeyis.DiscordBot;
import me.therealkeyis.models.UserInfo;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Implements the Link command which connects a minecraft username to
 * a discord username
 */
public class LinkDiscord implements CommandExecutor {
    Logger log;

    /**
     * Creates a new link
     * 
     * @param logger The logger for the plugin
     */
    public LinkDiscord(Logger logger) {
        this.log = logger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            var pattern = Pattern.compile("\\w+#\\d{4}");
            if (label.equals("link"))
                sender.sendMessage(
                        "Warning: This command is deprecated and will be removed in a future update. Use /discord instead.");

            if (pattern.matcher(args[0]).find()) {
                var info = new UserInfo(args[0], sender.getName(), DiscordBot.getInstance().getGuild(args[0]));
                Database.getInstance().writeNewUserEntry(info);
                sender.sendMessage("Successfully linked " + sender.getName() + " to " + args[0]);
                return true;
            }
        }
        return false;
    }
}
