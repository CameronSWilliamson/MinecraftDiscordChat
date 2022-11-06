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
    private String serverId;
    private FileConfiguration config;
    private Logger log;
    private Runnable saveConfig;

    /**
     * Automatically builds configuration based on the Plugin configuration
     * 
     * @param config     The plugin configuration object
     * @param log        The plugin logger
     * @param saveConfig A function that saves the current configuration to file
     */
    public DiscordConfig(FileConfiguration config, Logger log, Runnable saveConfig) {
        token = config.getString("api_token");
        textChannelId = config.getString("default_text_channel_id");
        voiceChannelId = config.getString("default_voice_channel_id");
        category = config.getString("default_category_name");
        serverId = config.getString("default_server_id");
        this.log = log;
        this.config = config;
        this.saveConfig = saveConfig;
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
     * Provides the configured server id
     * 
     * @return A Discord server ID
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * The plugin logger
     * 
     * @return The plugin logger
     */
    public Logger getLogger() {
        return log;
    }

    public void setTextChannelId(String textChannelId) {
        this.textChannelId = textChannelId;
        config.set("default_text_channel_id", textChannelId);
        saveConfig.run();
    }

    public void setVoiceChannelId(String voiceChannelId) {
        this.voiceChannelId = voiceChannelId;
        config.set("default_voice_channel_id", voiceChannelId);
        saveConfig.run();
    }

    public void setCategory(String category) {
        this.category = category;
        config.set("default_category_name", category);
        saveConfig.run();
    }

    public void setLog(Logger log) {
        this.log = log;
    }

}
