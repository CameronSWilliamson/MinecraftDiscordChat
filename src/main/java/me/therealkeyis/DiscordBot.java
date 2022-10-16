package me.therealkeyis;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Singleton that handles communications to and from the discord api
 */
public class DiscordBot {
    private static DiscordBot instance = null;
    private static String TOKEN = null;
    private static Long CHAT_CHANNEL = null;
    private static Logger log;
    private static String VOICE_CHANNEL = null;
    private Integer activePlayerCount = 0;
    private DiscordApi client;
    private HashMap<String, String> playerToChannel;

    /**
     * Builds an active DiscordClient.
     */
    private DiscordBot() {
        playerToChannel = new HashMap<>();
        client = new DiscordApiBuilder().setToken(TOKEN).setIntents(Intent.GUILD_MESSAGES, Intent.GUILD_MEMBERS).login()
                .join();
        log.info("Connected to discord");

        client.addMessageCreateListener(event -> {
            log.info("Recieved Message");
            if (event.getMessageAuthor().isBotUser()) {
                return;
            }
            if (event.getChannel().getId() == CHAT_CHANNEL) {
                Bukkit.broadcastMessage(
                        "<" + event.getMessageAuthor().getDisplayName() + "> " +
                                event.getMessageContent());
            }
        });

        updateActivePlayerCount();

    }

    /**
     * Sends a message to the channel saved in CHAT_CHANNEL.
     *
     * @param content The text to send to the channel.
     */
    public void sendMessage(String content) {
        Optional<Channel> channel_opt = client.getChannelById(CHAT_CHANNEL);
        if (channel_opt.isPresent()) {
            Optional<TextChannel> txt_channel_opt = channel_opt.get().asTextChannel();
            txt_channel_opt.ifPresent(textChannel -> new MessageBuilder().append(content).send(textChannel));
        }
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
     * @throws NumberFormatException Thrown when chat_channel cannot be turned into
     *                               a long
     */
    public static void configureInstance(String discord_token, String chat_channel, String default_voice, Logger log)
            throws NumberFormatException {
        DiscordBot.TOKEN = discord_token;
        DiscordBot.VOICE_CHANNEL = default_voice;
        DiscordBot.CHAT_CHANNEL = Long.parseLong(chat_channel);
        DiscordBot.log = log;
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
        movePlayer(name, VOICE_CHANNEL);
    }

    /**
     * Increases the active player count for the discord status
     */
    public void increaseActivePlayerCount() {
        activePlayerCount += 1;
        updateActivePlayerCount();
    }

    /**
     * 
     */
    public void decreaseActivePlayerCount() {
        activePlayerCount -= 1;
        updateActivePlayerCount();
    }

    private void updateActivePlayerCount() {
        client.updateActivity(ActivityType.WATCHING, activePlayerCount + " players");
    }
}
