package me.therealkeyis.models;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * A pair of coordinates
 */
public class LocationPair {
    /**
     * The x value for the location
     */
    public double x;

    /**
     * The z value for the location
     */
    public double z;

    /**
     * Creates a location pair from a PlayerInteractionEvent
     * 
     * @param event A player interaction event
     */
    public LocationPair(PlayerInteractEvent event) {
        var location = event.getClickedBlock().getLocation();
        x = location.getX();
        z = location.getZ();
    }

    /**
     * Creates a location pair from the provided coordinates
     * 
     * @param x the x coordinate
     * @param z the z coordinate
     */
    public LocationPair(double x, double z) {
        this.x = x;
        this.z = z;
    }
}