package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.therealkeyis.mcCommands.Link;
import me.therealkeyis.mcCommands.Request;
import me.therealkeyis.mcCommands.VoiceArea;
import me.therealkeyis.models.DiscordConfig;

import java.util.Objects;

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
        Sqlite.configureInstance(getDataFolder().getAbsolutePath(), getLogger());
        defaultConfig();
        DiscordBot.configureInstance(new DiscordConfig(config, getLogger()));
        bot = DiscordBot.getInstance();
        registerEvents();
    }

    /**
     * Registers all event listeners and command executors for the plugin
     */
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new McToDcListener(bot, getLogger()), this);
        getServer().getPluginManager().registerEvents(new UserListener(getLogger()), this);
        Objects.requireNonNull(getCommand("request")).setExecutor(new Request());
        Objects.requireNonNull(getCommand("link")).setExecutor(new Link(getLogger()));
        Objects.requireNonNull(getCommand("voicearea")).setExecutor(new VoiceArea(this, getLogger()));
    }

    /**
     * Creates the default configuration file if there is no configuration file in
     * <server>/plugins/MinecraftDiscordChat
     */
    private void defaultConfig() {
        config.addDefault("discord_token", "Your Discord Bot Token");
        config.addDefault("discord_channel", "Your Discord Channel Token");
        config.addDefault("discord_voice", "Your default Discord Voice Channel id");
        config.addDefault("discord_category", "Minecraft Voice");
        saveDefaultConfig();
    }

    /**
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        bot.disconnect();
    }
}
