package me.therealkeyis;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import me.therealkeyis.models.DiscordConfig;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Singleton that handles communications to and from the discord api
 */
public class DiscordBot {
    /**
     * The global DiscordBot instance
     */
    private static DiscordBot instance = null;

    /**
     * The plugin logger
     */
    private static Logger log;

    /**
     * The configuration for the DiscordBot Instance
     */
    private static DiscordConfig config;

    /**
     * The active player count on the server
     */
    private Integer activePlayerCount = 0;

    /**
     * The discord api library object
     */
    private DiscordApi client;

    /**
     * A mapping of player to voice channel
     */
    private HashMap<String, String> playerToChannel;

    /**
     * Builds an active DiscordClient.
     */
    private DiscordBot() {
        playerToChannel = new HashMap<>();
        client = new DiscordApiBuilder().setToken(config.getToken())
                .setIntents(Intent.GUILD_MESSAGES, Intent.GUILD_MEMBERS).login()
                .join();
        log.info("Connected to discord");

        client.addMessageCreateListener(event -> {
            if (event.getMessageAuthor().isBotUser()) {
                return;
            }
            if (event.getChannel().getIdAsString().equals(config.getTextChannelId())) {
                Bukkit.broadcastMessage(
                        "<" + event.getMessageAuthor().getDisplayName() + "> " +
                                event.getMessageContent());
            }
        });

        verifyCategory();
        updateActivePlayerCount();
        migrateChannels();
    }

    /**
     * Checks if the configured category exists. If it doesn't exist then it creates
     * a category with the name specified in the config file.
     */
    private void verifyCategory() {
        var categories = client.getChannelCategoriesByName(config.getCategory());
        if (categories.size() == 0) {
            log.info("Was unable to find a category named " + config.getCategory() + " creating a new category");
            client.getChannelById(config.getTextChannelId())
                    .ifPresent(channel -> channel.asServerChannel().ifPresent(serverChan -> serverChan.getServer()
                            .createChannelCategoryBuilder().setName(config.getCategory()).create().join()));
        }
    }

    /**
     * This will verify that all channels that are used by this bot will be
     * inside of the "discord_category" named category.
     */
    public void migrateChannels() {
        var channelIdSet = Sqlite.getInstance().getChannelIdToChannelName().values();
        var channelIdList = new ArrayList<String>();
        for (var id : channelIdSet) {
            channelIdList.add(id);
        }
        var category = client.getChannelCategoriesByName(config.getCategory()).iterator().next();
        channelIdList.add(config.getVoiceChannelId());
        channelIdList.add(config.getTextChannelId());
        channelIdList.stream().forEach(channelId -> {
            client.getChannelById(channelId).ifPresent(channel -> {
                channel.asServerVoiceChannel().ifPresent(schan -> schan.updateCategory(category));
                channel.asServerTextChannel().ifPresent(schan -> schan.updateCategory(category));
            });
        });
    }

    /**
     * Sends a message to the channel saved in CHAT_CHANNEL.
     *
     * @param content The text to send to the channel.
     */
    public void sendMessage(String content) {
        client.getChannelById(config.getTextChannelId())
                .ifPresent(channel -> channel.asTextChannel().ifPresent(text -> text.sendMessage(content)));
    }

    /**
     * Sends a message to the developer on discord
     * 
     * @param requestUser The user sending the message
     * @param content     The content in the message
     */
    public void messageDev(String requestUser, String content) {
        User user = client.getUserById(153353058514894848L).join();
        user.sendMessage(requestUser + ": " + content);
    }

    /**
     * Creates a discord voice channel with the provided channelName on the
     * server with the associated serverId
     * 
     * @param channelName The name of the new channel
     * @param serverId    The server id of the server to create the channel
     * @return Returns the channelId of the new channel
     */
    public String createChannel(String channelName, String serverId) {
        var serverOpt = client.getServerById(serverId);
        if (!serverOpt.isPresent())
            return "";
        var server = serverOpt.get();
        var builder = server.createVoiceChannelBuilder();
        builder.setName(channelName);
        try {
            return builder.create().get().getIdAsString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the guild id from a discord username with id
     * 
     * @param usernameWithId The discord username with an id
     * @return A discord guild id as a string
     */
    public String getGuild(String usernameWithId) {
        var userOptional = client.getCachedUserByDiscriminatedName(usernameWithId);
        if (!userOptional.isPresent()) {
            return "";
        }
        var user = userOptional.get();
        var server = (Server) user.getMutualServers().toArray()[0];
        return server.getIdAsString();
    }

    /**
     * Fetches the current DiscordBot instance.
     *
     * @return Static DiscordBot instance
     */
    public static DiscordBot getInstance() {
        if (instance == null) {
            instance = new DiscordBot();
        }
        return instance;
    }

    /**
     * Configures variables required for discord interaction.
     *
     * @param discord_token The token of the Discord Bot
     * @param chat_channel  The channel to send messages to
     * @param default_voice The default voice channel
     * @param log           The plugin logger
     * @throws NumberFormatException Thrown when chat_channel cannot be turned into
     *                               a long
     */
    public static void configureInstance(DiscordConfig config) {
        DiscordBot.config = config;
        DiscordBot.log = config.getLogger();
    }

    /**
     * Moves a player from one voice chat to another
     * 
     * @param name      The username of the player to move
     * @param channelId The channel id of the channel to move them to
     */
    public void movePlayer(String name, String channelId) {
        if (playerToChannel.containsKey(name))
            if (playerToChannel.get(name).equals(channelId))
                return;
        var channelOpt = client.getChannelById(channelId);
        if (!channelOpt.isPresent()) {
            return;
        }
        var channel = (ServerVoiceChannel) channelOpt.get();
        var userOpt = client.getCachedUserByDiscriminatedName(name);
        if (!userOpt.isPresent()) {
            return;
        }
        playerToChannel.put(name, channelId);
        userOpt.get().move(channel);
    }

    /**
     * Moves a player to the default voice channel
     * 
     * @param name The name of the player to move
     */
    public void movePlayerDefault(String name) {
        movePlayer(name, config.getVoiceChannelId());
    }

    /**
     * Increases the active player count for the discord status
     */
    public void increaseActivePlayerCount() {
        activePlayerCount += 1;
        updateActivePlayerCount();
    }

    /**
     * Decreases the active player count for the discord status
     */
    public void decreaseActivePlayerCount() {
        activePlayerCount -= 1;
        updateActivePlayerCount();
    }

    /**
     * Updates the active player count on the server
     */
    private void updateActivePlayerCount() {
        client.updateActivity(ActivityType.WATCHING, activePlayerCount + " players");
    }

    /**
     * Disconnects from the Discord API
     */
    public void disconnect() {
        client.disconnect();
    }
}
