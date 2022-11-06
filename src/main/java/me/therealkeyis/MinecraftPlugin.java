package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.therealkeyis.mcCommands.LinkDiscord;
import me.therealkeyis.mcCommands.Request;
import me.therealkeyis.mcCommands.CreateVoice;
import me.therealkeyis.mcCommands.DeleteVoice;
import me.therealkeyis.models.DiscordConfig;

import java.util.Objects;

/**
 * Discord Chat Minecraft Plugin
 *
 * Main class that handles chat interactions
 */
public class MinecraftPlugin extends JavaPlugin {
    /**
     * A Discord Bot Instance
     */
    DiscordBot bot;
    /**
     * The plugin configuration
     */
    FileConfiguration config = this.getConfig();
    /**
     * The default discord token value
     */
    public static final String DEFAULT_DISCORD_TOKEN = "Your Discord Bot Token";
    /**
     * The default discord channel value
     */
    public static final String DEFAULT_DISCORD_CHANNEL = "Your Discord Channel Token";
    /**
     * The default discord voice value
     */
    public static final String DEFAULT_DISCORD_VOICE = "Default voice channel";
    /**
     * The default discord category value
     */
    public static final String DEFAULT_DISCORD_CATEGORY = "Minecraft Voice";
    /**
     * The default discord server value
     */
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
        Objects.requireNonNull(getCommand("link")).setExecutor(new LinkDiscord(getLogger()));
        Objects.requireNonNull(getCommand("discord")).setExecutor(new LinkDiscord(getLogger()));
        Objects.requireNonNull(getCommand("voicearea")).setExecutor(new CreateVoice(this, getLogger()));
        Objects.requireNonNull(getCommand("createvoice")).setExecutor(new CreateVoice(this, getLogger()));
        Objects.requireNonNull(getCommand("deletevoice")).setExecutor(new DeleteVoice(getLogger()));

    }

    /**
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
