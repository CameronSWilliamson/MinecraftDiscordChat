package me.therealkeyis;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.therealkeyis.mcCommands.Link;
import me.therealkeyis.mcCommands.Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Discord Chat Minecraft Plugin
 *
 * Main class that handles chat interactions
 */
public class MinecraftPlugin extends JavaPlugin {
    DiscordBot bot;
    FileConfiguration config = this.getConfig();
    // String dataPath = getDataFolder().getPath();
    Connection connection = null;
    // SQLiteDB database = new SQLiteDB(dataPath);

    /**
     * Handles initialization of the plugin
     *
     * Sets up defaults and configures the DiscordBot class
     */
    @Override
    public void onEnable() {
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
        Objects.requireNonNull(getCommand("request")).setExecutor(new Request());
        Objects.requireNonNull(getCommand("link")).setExecutor(new Link());
        initializeSQL();
    }

    private void defaultConfig() {
        config.addDefault("discord_token", "Your Discord Bot Token");
        config.addDefault("discord_channel", "Your Discord Channel Token");
        saveDefaultConfig();
    }

    private void initializeSQL() {
        var url = "jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/mcdc.sqlite3";
        var createTable = "CREATE TABLE IF NOT EXISTS McToDiscord (discord TEXT, minecraft TEXT, PRIMARY KEY (discord, minecraft));";

        try {
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                var meta = connection.getMetaData();
                getLogger().info("Created new SQLite database using driver " + meta.getDriverName());
            }
            var stmt = connection.createStatement();
            stmt.executeUpdate(createTable);
            stmt.close();
        } catch (SQLException e) {
            getLogger().warning("Unable to connect to sqlite3 database");
        }
    }

    /**
     * Executes cleanup on server stop
     */
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
