package me.therealkeyis.mcCommands;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.therealkeyis.Database;
import me.therealkeyis.DiscordBot;

/**
 * The delete voice command handler
 */
public class DeleteVoice implements CommandExecutor {
    /**
     * The plugin logger
     */
    private Logger log;

    /**
     * Creates a new delete voice command
     * 
     * @param log The plugin logger
     */
    public DeleteVoice(Logger log) {
        this.log = log;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var channelName = String.join(" ", args);
        var channelId = Database.getInstance().getChannelId(channelName);
        if (DiscordBot.getInstance().deleteChannel(channelId)) {
            if (Database.getInstance().deleteVoiceArea(channelName)) {
                log.info("Removed " + channelName + " from the database");
                sender.sendMessage("Successfully Deleted " + channelName);
                return true;
            }
        }
        return false;
    }
}
