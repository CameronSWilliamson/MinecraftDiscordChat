package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.therealkeyis.mcCommands.Link;
import me.therealkeyis.mcCommands.Request;
import me.therealkeyis.mcCommands.VoiceArea;

import java.util.Objects;

/**
 * Discord Chat Minecraft Plugin
 *
 * Main class that handles chat interactions
 */
public class MinecraftPlugin extends JavaPlugin {
    DiscordBot bot;
    FileConfiguration config = this.getConfig();
    Sqlite sqlite;

    /**
     * Handles initialization of the plugin
     *
     * Sets up defaults and configures the DiscordBot class
     */
    @Override
    public void onEnable() {
        sqlite = new Sqlite(getDataFolder().getAbsolutePath(), getLogger());
        defaultConfig();
        try {
            DiscordBot.configureInstance(config.getString("discord_token"), config.getString("discord_channel"),
                    getLogger());
        } catch (NumberFormatException ex) {
            getLogger().warning("Unable to parse channel string, did you forget to set it? Disabling chat listener.");
            return;
        }
        bot = DiscordBot.getInstance();
        getServer().getPluginManager().registerEvents(new McToDcListener(bot), this);
        getServer().getPluginManager().registerEvents(new UserListener(getLogger(), sqlite), this);
        Objects.requireNonNull(getCommand("request")).setExecutor(new Request());
        Objects.requireNonNull(getCommand("link")).setExecutor(new Link(sqlite, getLogger()));
        Objects.requireNonNull(getCommand("voicearea")).setExecutor(new VoiceArea(this));
    }

    private void defaultConfig() {
        config.addDefault("discord_token", "Your Discord Bot Token");
        config.addDefault("discord_channel", "Your Discord Channel Token");
        saveDefaultConfig();
    }

    /**
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
