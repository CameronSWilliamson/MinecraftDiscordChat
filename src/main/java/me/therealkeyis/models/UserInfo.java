package me.therealkeyis.models;

/**
 * A set of user information that connects their usernames and guild id
 */
public class UserInfo {
    private String discordUsername;
    private String minecraftUsername;
    private String guildId;

    /**
     * Creates a new instance of a UserInfo object
     * 
     * @param discord   A user's Discord username
     * @param minecraft A user's Minecraft username
     * @param guild     A user's Discord guildId
     */
    public UserInfo(String discord, String minecraft, String guild) {
        discordUsername = discord;
        minecraftUsername = minecraft;
        guildId = guild;
    }

    /**
     * Gets a user's Discord username
     * 
     * @return A user's Discord username
     */
    public String getDiscordUsername() {
        return discordUsername;
    }

    /**
     * Gets a user's Minecraft username
     * 
     * @return A user's Minecraft username
     */
    public String getMinecraftUsername() {
        return minecraftUsername;
    }

    /**
     * Gets a user's Minecraft username
     * 
     * @return A user's Minecraft username
     */
    public String getGuildId() {
        return guildId;
    }
}
