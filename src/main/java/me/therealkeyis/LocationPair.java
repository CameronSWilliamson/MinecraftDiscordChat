package me.therealkeyis;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * A pair of coordinates
 */
public class LocationPair {
    public double x;
    public double z;

    public LocationPair(PlayerInteractEvent event) {
        var location = event.getClickedBlock().getLocation();
        x = location.getX();
        z = location.getZ();
    }

    public LocationPair(double x, double z) {
        this.x = x;
        this.z = z;
    }
}