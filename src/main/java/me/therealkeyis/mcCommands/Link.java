package me.therealkeyis.mcCommands;

import me.therealkeyis.DiscordBot;
import me.therealkeyis.Sqlite;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Link implements CommandExecutor {
    Sqlite sqlite;
    Logger log;

    public Link(Sqlite sqlite, Logger logger) {
        this.sqlite = sqlite;
        this.log = logger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            var pattern = Pattern.compile("\\w+#\\d{4}");
            if (pattern.matcher(args[0]).find()) {
                log.info("Writing to database");
                return sqlite.linkUsernames(args[0], sender.getName(), DiscordBot.getInstance().getGuild(args[0]));
            }
        }
        return false;
    }
}
