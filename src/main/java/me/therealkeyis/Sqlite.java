package me.therealkeyis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Sqlite {
    private final String path;
    private Connection con;
    private final Logger log;

    public Sqlite(String folderPath, Logger log) {
        this.log = log;
        path = folderPath + "/mcdc.sqlite3";

        var url = "jdbc:sqlite:" + path;
        try {
            con = DriverManager.getConnection(url);
            if (con != null) {
                log.info("Connected to database");
            }
            createTables();
        } catch (SQLException e) {
            log.warning("Unable to connect to sqlite3 database");
        }
    }

    private void createTables() {
        var createTable = "CREATE TABLE IF NOT EXISTS McToDiscord (discord TEXT, minecraft TEXT, PRIMARY KEY (discord, minecraft));";
        if (con != null) {
            try (var stmt = con.createStatement()) {
                stmt.executeUpdate(createTable);
                stmt.close();
            } catch (SQLException e) {
                log.warning("Failed to create tables");
            }
        }
    }

    public boolean linkUsernames(String discord, String minecraft) {
        var insertStmt = "INSERT INTO McToDiscord (discord, minecraft) values (?, ?)";
        try (var prep = con.prepareStatement(insertStmt)) {
            prep.setString(1, discord);
            prep.setString(2, minecraft);
            prep.executeUpdate();
        } catch (SQLException e) {
            log.warning(e.toString());
            return false;
        }
        return true;
    }
}
