package me.therealkeyis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TutorialPlugin extends JavaPlugin {
    DiscordBot bot;

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    private class ChatListener implements Listener {
        DiscordBot bot = DiscordBot.getInstance();

        @EventHandler
        public void onPlayerChat(AsyncPlayerChatEvent event) {
            Player player = event.getPlayer();
            String message = event.getMessage();
            getLogger().info("recieved chat");
            bot.sendMessage(player.getName() + ": " + message);
        }
    }
}
