package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Discord Chat Minecraft Plugin
 *
 * Main class that handles chat interactions
 */
public class MinecraftPlugin extends JavaPlugin {
    DiscordBot bot;
    FileConfiguration config = this.getConfig();

    /**
     * Handles initialization of the plugin
     *
     * Sets up defaults and configures the DiscordBot class
     */
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
        bot = DiscordBot.getInstance();
        getServer().getPluginManager().registerEvents(new McToDcListener(bot), this);
    }

    /**
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
