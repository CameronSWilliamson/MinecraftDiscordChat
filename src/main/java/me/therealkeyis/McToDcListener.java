package me.therealkeyis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles all listen interactions from the bukkit library
 */
public class McToDcListener implements Listener {
    private final DiscordBot bot;

    public McToDcListener(DiscordBot bot) {
        this.bot = bot;
    }

    /**
     * Increases player count in discord bot when a player joins the server
     *
     * @param event The player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        bot.increaseActivePlayerCount();
    }

    /**
     * Decreases player count in discord bot when a player leaves the server
     *
     * @param event The player leave event
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        bot.decreaseActivePlayerCount();
    }

    /**
     * Relays player chat when the user sends a chat message
     *
     * @param event The player chat event
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        bot.sendMessage(player.getName() + ": " + message);
    }
}
