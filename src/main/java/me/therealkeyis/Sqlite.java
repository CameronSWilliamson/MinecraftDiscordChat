package me.therealkeyis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import me.therealkeyis.models.LocationEntry;
import me.therealkeyis.models.UserInfo;

/**
 * Singleton that handles all SQL interactions
 */
public class Sqlite {
    /**
     * The default sqlite instance
     */
    private static Sqlite instance;

    /**
     * The absolute path of the sqlite database
     */
    private static String path;

    /**
     * The plugin logger
     */
    private static Logger log;

    /**
     * The database connection
     */
    private Connection con;

    /**
     * Creates a new global sqlite instance
     */
    private Sqlite() {
        try {
            log.info(path);
            con = DriverManager.getConnection("jdbc:sqlite:" + path);
            log.info("Connected to database");
            createTables();
        } catch (SQLException e) {
            log.warning("Unable to connect to database");
        }
    }

    /**
     * Creates all tables in the sqlite3 database
     */
    private void createTables() {
        var createUserMap = "CREATE TABLE IF NOT EXISTS McToDiscord (discord TEXT, minecraft TEXT, guild TEXT, PRIMARY KEY (discord, minecraft));";
        var createLocation = "CREATE TABLE IF NOT EXISTS Locations (discord_vc TEXT, x1 double, z1 double, x2 double, z2 double, PRIMARY KEY (discord_vc));";
        var createLocationToId = "CREATE TABLE IF NOT EXISTS LocationToId (vc_id TEXT, discord_vc TEXT, PRIMARY KEY (vc_id, discord_vc))";

        if (con != null) {
            try (var stmt = con.createStatement()) {
                stmt.executeUpdate(createUserMap);
                stmt.executeUpdate(createLocation);
                stmt.executeUpdate(createLocationToId);
                stmt.close();
            } catch (SQLException e) {
                log.warning("Failed to create tables");
            }
        }
    }

    /**
     * Gets all of the location entries in the database
     * 
     * @return A list of location entries
     */
    public List<LocationEntry> getLocationEntries() {
        List<LocationEntry> locationEntries = new ArrayList<>();
        try (var stmt = con.createStatement()) {
            var rs = stmt.executeQuery("SELECT * FROM Locations;");
            while (rs.next()) {
                var name = rs.getString(1);
                var x1 = rs.getDouble(2);
                var z1 = rs.getDouble(3);
                var x2 = rs.getDouble(4);
                var z2 = rs.getDouble(5);
                locationEntries.add(new LocationEntry(name, x1, z1, x2, z2));
            }
        } catch (SQLException e) {
            log.warning(e.toString());
        }
        return locationEntries;
    }

    public List<UserInfo> getUserInfo() {
        List<UserInfo> userInfos = new ArrayList<>();
        try (var stmt = con.createStatement()) {
            var rs = stmt.executeQuery("SELECT * FROM McToDiscord;");
            while (rs.next()) {
                userInfos.add(new UserInfo(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            log.warning(e.toString());
        }
        return userInfos;
    }

    /**
     * Binds a location in the minecraft world to
     *
     * @param entry a location entry object
     * @return True of successful, false otherwise
     */
    public boolean writeLocationToChannel(LocationEntry entry) {
        var insertStmt = "INSERT INTO Locations (discord_vc, x1, z1, x2, z2) values (?, ?, ?, ?, ?);";
        try (var prep = con.prepareStatement(insertStmt)) {
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
     * Creates a map of channelId to LocationEntry names
     * 
     * @return map
     */
    public Map<String, String> getChannelIdToChannelName() {
        Map<String, String> channelMap = new HashMap<>();
        try (var stmt = con.createStatement()) {
            var rs = stmt.executeQuery("SELECT * FROM LocationToId");
            while (rs.next()) {
                var name = rs.getString(1);
                var id = rs.getString(2);
                channelMap.put(name, id);
            }
        } catch (SQLException e) {
            log.warning(e.toString());
        }
        return channelMap;
    }

    /**
     * Links minecraft and discord usernames
     * 
     * @param discord   A user's discord username
     * @param minecraft A user's minecraft username
     * @param guild     The guild they share with the bot
     * @return Returns true if successful, false otherwise
     */
    public boolean linkUsernames(String discord, String minecraft, String guild) {
        var insertStmt = "INSERT INTO McToDiscord (discord, minecraft, guild) values (?, ?, ?)";
        try (var prep = con.prepareStatement(insertStmt)) {
            prep.setString(1, discord);
            prep.setString(2, minecraft);
            prep.setString(3, guild);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Links an area name with a discord channel id
     * 
     * @param vc_name    The name of the location / voice chat
     * @param channel_id The discord id for the voice channel
     * @return True if successful, false otherwise
     */
    public boolean createChannel(String vc_name, String channel_id) {
        var insertStmt = "INSERT INTO LocationToId (vc_id, discord_vc) VALUES (?, ?)";
        try (var prep = con.prepareStatement(insertStmt)) {
            prep.setString(1, vc_name);
            prep.setString(2, channel_id);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

    // /**
    // * Gets the guild associated with the provided minecraft username
    // *
    // * @param mcUsername A player's minecraft username
    // * @return The guild id for the server the player is in. Empty string if none
    // */
    // public String getGuildFromMCUser(String mcUsername) {
    // var selectStmt = "SELECT guild FROM McToDiscord where minecraft = ?";

    // try (var prep = con.prepareStatement(selectStmt)) {
    // prep.setString(1, mcUsername);
    // var res = prep.executeQuery();
    // if (res.next()) {
    // return res.getString(1);
    // }
    // } catch (SQLException e) {
    // log.warning(e.toString());
    // return "";
    // }
    // return "";
    // }

    // /**
    // * Gets a user's discord name and number using their minecraft username
    // *
    // * @param mcUsername A player's minecraft username
    // * @return The name#number of a user or empty string if DNE
    // */
    // public String getDiscordFromUser(String mcUsername) {
    // var selectStmt = "SELECT discord FROM McToDiscord where minecraft = ?";

    // try (var prep = con.prepareStatement(selectStmt)) {
    // prep.setString(1, mcUsername);
    // var res = prep.executeQuery();
    // if (res.next()) {
    // return res.getString(1);
    // }
    // } catch (SQLException e) {
    // log.warning(e.toString());
    // return "";
    // }
    // return "";
    // }

    /**
     * Configures the path and the logger for the global Sqlite instance
     * 
     * @param path The path of the data directory
     * @param log  The plugin logger
     */
    public static void configureInstance(String path, Logger log) {
        Sqlite.log = log;
        Sqlite.path = path + "/mcdc.sqlite3";
    }

    /**
     * Gets the global Sqlite instance
     * 
     * @return an sqlite instance
     */
    public static Sqlite getInstance() {
        if (instance == null)
            instance = new Sqlite();

        return instance;
    }
}
