package me.therealkeyis;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;

import java.util.Optional;
import java.util.logging.Logger;

public class DiscordBot {
    private static DiscordBot instance = null;
    private static String TOKEN = null;
    private static Long CHAT_CHANNEL = null;
    DiscordApi client;

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
    }

    public void sendMessage(String content) {
        Optional<Channel> channel_opt = client.getChannelById(CHAT_CHANNEL);
        if (channel_opt.isPresent()) {
            Optional<TextChannel> txt_channel_opt = channel_opt.get().asTextChannel();
            txt_channel_opt.ifPresent(textChannel -> new MessageBuilder().append(content).send(textChannel));
        }
    }

    public static DiscordBot getInstance() {
        if (instance == null) {
            instance = new DiscordBot();
        }
        return instance;
    }

    public static void configureInstance(String discord_token, String chat_channel) throws NumberFormatException {
        DiscordBot.TOKEN = discord_token;
        DiscordBot.CHAT_CHANNEL = Long.parseLong(chat_channel);
    }
}
