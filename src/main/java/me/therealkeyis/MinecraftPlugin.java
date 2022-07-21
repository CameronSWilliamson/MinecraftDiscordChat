package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftPlugin extends JavaPlugin {
    DiscordBot bot;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        config.addDefault("discord_token", "Your Discord Bot Token");
        config.addDefault("discord_channel", "Your Discord Channel Token");
        saveDefaultConfig();
        getLogger().info("onEnable is called!");
        try {
            DiscordBot.configureInstance(config.getString("discord_token"), config.getString("discord_channel"));
        } catch (NumberFormatException ex) {
            getLogger().warning("Unable to parse channel string, did you forget to set it? Disabling chat listener.");
            return;
        }
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    private static class ChatListener implements Listener {
        DiscordBot bot = DiscordBot.getInstance();

        @EventHandler
        public void onPlayerChat(AsyncPlayerChatEvent event) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            bot.sendMessage(player.getName() + ": " + message);
        }
    }
}
