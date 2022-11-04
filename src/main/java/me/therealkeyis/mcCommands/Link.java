package me.therealkeyis.mcCommands;

import me.therealkeyis.Cache;
import me.therealkeyis.DiscordBot;
import me.therealkeyis.Sqlite;
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
public class Link implements CommandExecutor {
    Sqlite sqlite;
    Logger log;

    /**
     * Creates a new link
     * 
     * @param logger The logger for the plugin
     */
    public Link(Logger logger) {
        this.sqlite = Sqlite.getInstance();
        this.log = logger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            var pattern = Pattern.compile("\\w+#\\d{4}");
            if (pattern.matcher(args[0]).find()) {
                var info = new UserInfo(args[0], sender.getName(), DiscordBot.getInstance().getGuild(args[0]));
                Cache.getInstance().writeNewUserEntry(info);
                return true;
            }
        }
        return false;
    }
}
