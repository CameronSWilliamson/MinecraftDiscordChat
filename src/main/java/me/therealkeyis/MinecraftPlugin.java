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
    public static final String DEFAULT_DISCORD_TOKEN = "Your Discord Bot Token";
    public static final String DEFAULT_DISCORD_CHANNEL = "Your Discord Channel Token";
    public static final String DEFAULT_DISCORD_VOICE = "Default voice channel";
    public static final String DEFAULT_DISCORD_CATEGORY = "Minecraft Voice";
    public static final String DEFAULT_DISCORD_SERVER = "Discord Server";

    /**
     * Handles initialization of the plugin
     *
     * Sets up defaults and configures the DiscordBot class
     */
    @Override
    public void onEnable() {
        Database.configureInstance(getDataFolder().getAbsolutePath(), getLogger());
        saveDefaultConfig();
        DiscordBot.configureInstance(new DiscordConfig(config, getLogger(), () -> saveConfig()));
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
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
