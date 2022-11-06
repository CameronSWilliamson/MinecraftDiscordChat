package me.therealkeyis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.tools.DocumentationTool.Location;

import me.therealkeyis.models.LocationEntry;
import me.therealkeyis.models.LocationPair;
import me.therealkeyis.models.UserInfo;

/**
 * Manages the storage and retrieval of all data necessary for the application
 * that is not considered configuration data
 */
public class Database {
    /* Static Variables */
    /**
     * The global Database instance
     */
    private static Database instance;
    /**
     * The plugin logger
     */
    private static Logger log;
    /**
     * The path to the database
     */
    private static String path;
    /* Instance variables */
    /**
     * A list of location entries as a write-through cache
     */
    private List<LocationEntry> locationEntries;
    /**
     * A map of channel names to channel ids as a write-through cache
     */
    private Map<String, String> channelNameToId;
    /**
     * A list of user information as a write-through cache
     */
    private List<UserInfo> userInfos;
    /**
     * The database connection
     */
    private Connection connection;

    /**
     * Creates a new global database instance
     */
    private Database() {
        locationEntries = new ArrayList<>();
        channelNameToId = new HashMap<>();
        userInfos = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            createTables();
            loadData();
        } catch (SQLException e) {
            log.warning("Unable to connect and initialize database: " + e.toString());
        }
    }

    /**
     * Creates all of the required tables for the application
     * 
     * @throws SQLException
     */
    private void createTables() throws SQLException {
        var stmt = connection.createStatement();
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS McToDiscord (discord TEXT, minecraft TEXT, guild TEXT, PRIMARY KEY (discord, minecraft));");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Locations (discord_vc TEXT, x1 double, z1 double, x2 double, z2 double, PRIMARY KEY (discord_vc));");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS LocationToId (vc_id TEXT, discord_vc TEXT, PRIMARY KEY (vc_id, discord_vc))");
        stmt.close();
    }

    /**
     * Loads all of the data from the database into a cache inside of memory
     * 
     * @throws SQLException No table found errors
     */
    private void loadData() throws SQLException {
        var rs = connection.createStatement().executeQuery("SELECT * FROM Locations;");
        while (rs.next())
            locationEntries.add(new LocationEntry(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4),
                    rs.getDouble(5)));

        rs = connection.createStatement().executeQuery("SELECT * FROM McToDiscord;");
        while (rs.next())
            userInfos.add(new UserInfo(rs.getString(1), rs.getString(2), rs.getString(3)));

        rs = connection.createStatement().executeQuery("SELECT * FROM LocationToId");
        while (rs.next())
            channelNameToId.put(rs.getString(1), rs.getString(2));
    }

    /**
     * Writes a new location entry to the database
     * 
     * @param entry The entry to be written
     * @return true if successful, false otherwise
     */
    public boolean writeNewLocationEntry(LocationEntry entry) {
        locationEntries.add(entry);
        var insertStmt = "INSERT INTO Locations (discord_vc, x1, z1, x2, z2) values (?, ?, ?, ?, ?);";
        try (var prep = connection.prepareStatement(insertStmt)) {
            prep.setString(1, entry.locationName);
            prep.setDouble(2, entry.x1);
            prep.setDouble(3, entry.z1);
            prep.setDouble(4, entry.x2);
            prep.setDouble(5, entry.z2);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Writes a new UserInfo entry to the database
     * 
     * @param entry The entry to be written
     * @return true if successful, false otherwise
     */
    public boolean writeNewUserEntry(UserInfo entry) {
        userInfos.add(entry);
        var insertStmt = "INSERT INTO McToDiscord (discord, minecraft, guild) values (?, ?, ?)";
        try (var prep = connection.prepareStatement(insertStmt)) {
            prep.setString(1, entry.getDiscordUsername());
            prep.setString(2, entry.getMinecraftUsername());
            prep.setString(3, entry.getGuildId());
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Writes a new Channel entry to the database
     * 
     * @param channelName The channel name
     * @param channelId   The channel id
     * @return true if successful, false otherwise
     */
    public boolean writeNewChannelEntry(String channelName, String channelId) {
        channelNameToId.put(channelName, channelId);
        var insertStmt = "INSERT INTO LocationToId (vc_id, discord_vc) VALUES (?, ?)";
        try (var prep = connection.prepareStatement(insertStmt)) {
            prep.setString(1, channelName);
            prep.setString(2, channelId);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Gets the location that surrounds the provided position
     * 
     * @param position A player's position
     * @return A location entry that surrounds the provieded location. null if none
     *         fit criteria
     */
    public LocationEntry getSurroundingLocation(LocationPair position) {
        for (var entry : locationEntries)
            if (entry.IsSurrounding(position))
                return entry;
        return null;
    }

    /**
     * Gets a channel id given a channel name
     * 
     * @param channelName The channel to look up
     * @return The channel id of the channel provided
     */
    public String getChannelId(String channelName) {
        if (channelNameToId.containsKey(channelName))
            return channelNameToId.get(channelName);
        return "";
    }

    /**
     * Gets all of the channelIds for the program
     * 
     * @return A list of channelIds
     */
    public List<String> getAllChannelIds() {
        return new ArrayList<>(channelNameToId.values());
    }

    /**
     * Gets the guild information for the provided minecraft username
     * 
     * @param minecraftUsername The player's username
     * @return The guild id of the channel the player shares with the bot
     */
    public String getGuildId(String minecraftUsername) {
        for (var entry : userInfos) {
            if (entry.getMinecraftUsername().equals(minecraftUsername))
                return entry.getGuildId();
        }
        return "";
    }

    /**
     * Gets a user's discord username based on the provided minecraft username
     * 
     * @param minecraftUsername The player's username
     * @return The user id of the player
     */
    public String getDiscordUsername(String minecraftUsername) {
        for (var entry : userInfos) {
            if (entry.getMinecraftUsername().equals(minecraftUsername))
                return entry.getDiscordUsername();
        }
        return "";
    }

    /**
     * Configures the Database instance
     * 
     * @param path The path to the database folder
     * @param log  The plugin logger
     */
    public static void configureInstance(String path, Logger log) {
        Database.log = log;
        Database.path = path + "/mcdc.sqlite3";
    }

    /**
     * Gets the global Database instance
     * 
     * @return Global instace
     */
    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }
}