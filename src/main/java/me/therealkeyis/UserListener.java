package me.therealkeyis;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

import me.therealkeyis.mcCommands.VoiceArea;

public class UserListener implements Listener {
    Logger log;
    Sqlite sqlite;

    public UserListener(Logger log) {
        this.log = log;
        this.sqlite = Sqlite.getInstance();
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;
        var p = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        var inv = p.getInventory();
        processItem(inv, new LocationPair(event), p.getDisplayName());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        var location = event.getTo();
        var newChannel = sqlite.getPossiblePositions(new LocationPair(location.getX(), location.getZ()));
        var playerDiscordName = sqlite.getDiscordFromUser(event.getPlayer().getDisplayName());
        if (newChannel.length() > 0) {
            var channelId = sqlite.getChannelIdFromName(newChannel);
            DiscordBot.getInstance().movePlayer(playerDiscordName, channelId);
        } else {
            DiscordBot.getInstance().movePlayerDefault(playerDiscordName);
        }
    }

    private void processItem(PlayerInventory inventory, LocationPair local, String playerName) {
        var item = inventory.getItemInMainHand();
        if (item.getType() != Material.STICK)
            return;
        if (!item.getItemMeta().getDisplayName().contains(VoiceArea.ItemName))
            return;
        if (VoiceArea.use_count == null)
            return;
        var meta = item.getItemMeta();
        var dataContainer = meta.getPersistentDataContainer();
        int counts;
        if (dataContainer.has(VoiceArea.use_count, PersistentDataType.INTEGER))
            counts = dataContainer.get(VoiceArea.use_count, PersistentDataType.INTEGER);
        else
            return;
        if (counts == 0) {
            dataContainer.set(VoiceArea.x1, PersistentDataType.DOUBLE, local.x);
            dataContainer.set(VoiceArea.z1, PersistentDataType.DOUBLE, local.z);
            dataContainer.set(VoiceArea.use_count, PersistentDataType.INTEGER, counts + 1);
            item.setItemMeta(meta);
        } else if (counts == 1) {
            double x1 = dataContainer.get(VoiceArea.x1, PersistentDataType.DOUBLE);
            double z1 = dataContainer.get(VoiceArea.z1, PersistentDataType.DOUBLE);
            var channelName = item.getItemMeta().getDisplayName().replace(VoiceArea.ItemName + " ", "");
            sqlite.writeLocationToChannel(channelName, x1,
                    z1, local.x, local.z);
            var channel = DiscordBot.getInstance().createChannel(channelName, sqlite.getGuildFromMCUser(playerName));
            sqlite.createChannel(channelName, channel);
            inventory.remove(item);
        }
    }

}
