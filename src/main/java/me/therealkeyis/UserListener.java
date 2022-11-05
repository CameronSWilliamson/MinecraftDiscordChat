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

/**
 * Listens to player movements and uses player movements to
 * coordinate discord voice channels
 */
public class UserListener implements Listener {
    /**
     * Logger for the plugin
     */
    private Logger log;

    /**
     * A sqlite connection
     */
    Sqlite sqlite;

    /**
     * Creates a new UserListener
     * 
     * @param log a plugin logger
     */
    public UserListener(Logger log) {
        this.log = log;
        this.sqlite = Sqlite.getInstance();
    }

    /**
     * When a player uses (right /left click) an item this event handler
     * is called. This event handler controls the configuration of
     * voice chat zones.
     * 
     * @param event The player use event
     */
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

    /**
     * When a player moves this event handler is called. This controls
     * the moving of players between voice channels.
     * 
     * @param event The movement event
     */
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

    /**
     * Changes metadata information for the item a player is holding if the
     * item is a stick and the name of the stick contains VoiceArea.ItemName.
     * This helps with keeping track of the number of times the item was used.
     * 
     * @param inventory  The inventory of a player
     * @param local      The location the player clicked
     * @param playerName The name of the player who clicked
     */
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
