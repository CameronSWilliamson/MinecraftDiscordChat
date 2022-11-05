package me.therealkeyis;

import java.util.List;
import java.util.Map;

import me.therealkeyis.models.LocationEntry;
import me.therealkeyis.models.LocationPair;
import me.therealkeyis.models.UserInfo;

/**
 * Stores database information in ram for faster access speeds
 */
public class Cache {
    /**
     * The global cache instance
     */
    public static Cache instance;
    /**
     * The sqlite database
     */
    private Sqlite db;
    /**
     * A list of locations
     */
    private List<LocationEntry> locationEntries;
    /**
     * A map of channel names to their ids
     */
    private Map<String, String> channelNameToId;
    /**
     * A list of user information
     */
    private List<UserInfo> userInfos;

    /**
     * Creates a new instance of the cache
     */
    private Cache() {
        db = Sqlite.getInstance();
        locationEntries = db.getLocationEntries();
        channelNameToId = db.getChannelIdToChannelName();
        userInfos = db.getUserInfo();
    }

    /**
     * Checks if the provided entry is inside any of the stored
     * location entries. If so the location entry is returned
     * 
     * @param position The position to check the location of
     * @return the location entry this belongs to
     */
    public LocationEntry getEntryBetween(LocationPair position) {
        for (var entry : locationEntries) {
            if (entry.IsBetween(position))
                return entry;
        }
        return null;
    }

    /**
     * Writes a new location entry to the cache (and database)
     * 
     * @param entry The entry to be written
     */
    public void writeNewLocationEntry(LocationEntry entry) {
        locationEntries.add(entry);
        db.writeLocationToChannel(entry);
    }

    public void writeNewUserEntry(UserInfo entry) {
        userInfos.add(entry);
        db.linkUsernames(entry.getDiscordUsername(), entry.getMinecraftUsername(), entry.getGuildId());
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
     * Writes a new channel entry
     * 
     * @param channelName The name of the channel
     * @param channelId   The ID of the channel
     */
    public void writeNewChannelEntry(String channelName, String channelId) {
        channelNameToId.put(channelName, channelId);
        db.createChannel(channelName, channelId);
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
     * Gets the global instance of the cache if it exists. Creates one otherwise
     * 
     * @return The global cache instance
     */
    public static Cache getInstance() {
        if (instance == null)
            instance = new Cache();
        return instance;
    }
}
