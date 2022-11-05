package me.therealkeyis.models;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Configuration object for the DiscordBot
 */
public class DiscordConfig {
    private String token;
    private String textChannelId;
    private String voiceChannelId;
    private String category;
    private Logger log;

    /**
     * Automatically builds configuration based on the Plugin configuration
     * 
     * @param config The plugin configuration object
     * @param log    The plugin logger
     */
    public DiscordConfig(FileConfiguration config, Logger log) {
        token = config.getString("discord_token");
        textChannelId = config.getString("discord_channel");
        voiceChannelId = config.getString("discord_voice");
        category = config.getString("discord_category");
        this.log = log;
        log.info("Doing something here");
    }

    /**
     * Provides the configured Discord API token
     * 
     * @return A Discord API token
     */
    public String getToken() {
        return token;
    }

    /**
     * Provides the configured default Discord text channel ID
     * 
     * @return A Discord text channel ID
     */
    public String getTextChannelId() {
        return textChannelId;
    }

    /**
     * Provides the configured default Discord voice channel ID
     * 
     * @return A Discord voice channel ID
     */
    public String getVoiceChannelId() {
        return voiceChannelId;
    }

    /**
     * Provides the configured default Discord voice category name
     * 
     * @return A Discord category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * The plugin logger
     * 
     * @return The plugin logger
     */
    public Logger getLogger() {
        return log;
    }
}
