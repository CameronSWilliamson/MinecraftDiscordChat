package me.therealkeyis;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

import me.therealkeyis.mcCommands.VoiceArea;

public class UserListener implements Listener {
    Logger log;
    Sqlite sqlite;

    public UserListener(Logger log, Sqlite sqlite) {
        this.log = log;
        this.sqlite = sqlite;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;
        var p = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        var inv = p.getInventory();
        processItem(inv, new LocationPair(event));
    }

    private void processItem(PlayerInventory inventory, LocationPair local) {
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
        log.info("Reading counts as " + counts);
        if (counts == 0) {
            dataContainer.set(VoiceArea.x1, PersistentDataType.DOUBLE, local.x);
            dataContainer.set(VoiceArea.z1, PersistentDataType.DOUBLE, local.z);
            dataContainer.set(VoiceArea.use_count, PersistentDataType.INTEGER, counts + 1);
            item.setItemMeta(meta);
        } else if (counts == 1) {
            double x1 = dataContainer.get(VoiceArea.x1, PersistentDataType.DOUBLE);
            double z1 = dataContainer.get(VoiceArea.z1, PersistentDataType.DOUBLE);
            sqlite.writeLocationToChannel(item.getItemMeta().getDisplayName().replace(VoiceArea.ItemName + " ", ""), x1,
                    z1, local.x, local.z);
            inventory.remove(item);
        }
    }

    class LocationPair {
        public double x;
        public double z;

        public LocationPair(PlayerInteractEvent event) {
            var location = event.getClickedBlock().getLocation();
            x = location.getX();
            z = location.getZ();
        }
    }
}
