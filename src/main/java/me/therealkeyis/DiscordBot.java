package me.therealkeyis;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;

import java.util.Optional;

/**
 * Handles communications to and from the discord api
 */
public class DiscordBot {
    private static DiscordBot instance = null;
    private static String TOKEN = null;
    private static Long CHAT_CHANNEL = null;
    private Integer activePlayerCount = 0;
    DiscordApi client;

    /**
     * Builds an active DiscordClient.
     */
    private DiscordBot() {
        client = new DiscordApiBuilder().setToken(TOKEN).login().join();
        client.addMessageCreateListener(event -> {
            if (event.getMessageAuthor().isBotUser()) {
                return;
            }
            TextChannel channel = event.getChannel();
            if (channel.getId() == CHAT_CHANNEL) {
                Bukkit.broadcastMessage("<"+event.getMessageAuthor().getDisplayName()+"> "+event.getMessageContent());
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

    public void messageDev(String requestUser, String content) {
        User user = client.getUserById(153353058514894848L).join();
        user.sendMessage(requestUser + ": " + content);
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
     * @param chat_channel The channel to send messages to
     * @throws NumberFormatException Thrown when chat_channel cannot be turned into a long
     */
    public static void configureInstance(String discord_token, String chat_channel) throws NumberFormatException {
        DiscordBot.TOKEN = discord_token;
        DiscordBot.CHAT_CHANNEL = Long.parseLong(chat_channel);
    }

    public void increaseActivePlayerCount() {
        activePlayerCount += 1;
        updateActivePlayerCount();
    }

    public void decreaseActivePlayerCount() {
        activePlayerCount -= 1;
        updateActivePlayerCount();
    }

    private void updateActivePlayerCount() {
        client.updateActivity(ActivityType.WATCHING, activePlayerCount + " players");
    }
}
