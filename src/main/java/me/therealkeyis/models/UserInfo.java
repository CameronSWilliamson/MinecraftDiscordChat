package me.therealkeyis.models;

public class UserInfo {
    private String discordUsername;
    private String minecraftUsername;
    private String guildId;

    public UserInfo(String discord, String minecraft, String guild) {
        discordUsername = discord;
        minecraftUsername = minecraft;
        guildId = guild;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public String getMinecraftUsername() {
        return minecraftUsername;
    }

    public String getGuildId() {
        return guildId;
    }
}
