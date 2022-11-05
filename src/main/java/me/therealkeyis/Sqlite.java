package me.therealkeyis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Singleton that handles all SQL interactions
 */
public class Sqlite {
    private static Sqlite instance;
    private static String path;
    private static Logger log;
    private Connection con;

    private Sqlite() {
        try {
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
     * Links minecraft and discord usernames
     * 
     * @param discord   A user's discord username
     * @param minecraft A user's minecraft username
     * @param guild     The guild they share with the bot
     * @return
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
     * Binds a location in the minecraft world to
     * 
     * @param vc
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @return
     */
    public boolean writeLocationToChannel(String vc, double x1, double z1, double x2, double z2) {
        var insertStmt = "INSERT INTO Locations (discord_vc, x1, z1, x2, z2) values (?, ?, ?, ?, ?);";
        try (var prep = con.prepareStatement(insertStmt)) {
            prep.setString(1, vc);
            prep.setDouble(2, x1);
            prep.setDouble(3, z1);
            prep.setDouble(4, x2);
            prep.setDouble(5, z2);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }

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

    public String getGuildFromMCUser(String mcUsername) {
        var selectStmt = "SELECT guild FROM McToDiscord where minecraft = ?";

        try (var prep = con.prepareStatement(selectStmt)) {
            prep.setString(1, mcUsername);
            var res = prep.executeQuery();
            if (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            log.warning(e.toString());
            return "";
        }
        return "";
    }

    public String getDiscordFromUser(String mcUsername) {
        var selectStmt = "SELECT discord FROM McToDiscord where minecraft = ?";

        try (var prep = con.prepareStatement(selectStmt)) {
            prep.setString(1, mcUsername);
            var res = prep.executeQuery();
            if (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            log.warning(e.toString());
            return "";
        }
        return "";
    }

    public String getChannelIdFromName(String name) {
        var selectStmt = "SELECT discord_vc FROM LocationToId where vc_id = ?";

        try (var prep = con.prepareStatement(selectStmt)) {
            prep.setString(1, name);
            var res = prep.executeQuery();
            if (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            log.warning(e.toString());
            return "";
        }
        return "";
    }

    public String getPossiblePositions(LocationPair currentLocation) {
        var selectStmt = "SELECT * from Locations";
        try (var stmt = con.createStatement()) {
            var rs = stmt.executeQuery(selectStmt);
            while (rs.next()) {
                // if currentLocation.x
                var name = rs.getString(1);
                var x1 = rs.getDouble(2);
                var z1 = rs.getDouble(3);
                var x2 = rs.getDouble(4);
                var z2 = rs.getDouble(5);
                boolean res = ((currentLocation.x < x1 && currentLocation.x > x2)
                        || (currentLocation.x > x1 && currentLocation.x < x2)) &&
                        ((currentLocation.z < z1 && currentLocation.z > z2)
                                || (currentLocation.z > z1 && currentLocation.z < z2));
                if (res)
                    return name;
                // if (currentLocation.x < x1 && currentLocation.x > x2)
            }
        } catch (SQLException e) {
            log.warning(e.toString());
            return "";
        }
        return "";
    }

    public static void configureInstance(String path, Logger log) {
        Sqlite.log = log;
        Sqlite.path = path;
    }

    public static Sqlite getInstance() {
        if (instance == null)
            instance = new Sqlite();

        return instance;
    }
}
