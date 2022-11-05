package me.therealkeyis;

/**
 * A database location entry
 */
public class LocationEntry {
    public String vc;
    public double x1;
    public double x2;
    public double z1;
    public double z2;

    /**
     * Creation of entry using two pairs of coordinates
     * 
     * @param vc     The name of the voice channel
     * @param coord1 The first pair of coordinates
     * @param coord2 The second pair of coordinates
     */
    public LocationEntry(String vc, LocationPair coord1, LocationPair coord2) {
        this.vc = vc;
        this.x1 = coord1.x;
        this.z1 = coord1.z;
        this.x2 = coord2.x;
        this.z2 = coord2.z;
    }

    /**
     * Creation of entry using raw coordinates
     * 
     * @param vc The name of the voice channel
     * @param x1 The first x coordinate
     * @param z1 The first z coordinate
     * @param x2 The second x coordinate
     * @param z2 The second z coordinate
     */
    public LocationEntry(String vc, double x1, double z1, double x2, double z2) {
        this.vc = vc;
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    /**
     * Checks if the provided coordinates are inside this location
     * 
     * @param playerx The player's x coordinate
     * @param playerz The player's z coordinate
     * @return True if inside, false otherwise
     */
    public boolean IsBetween(double playerx, double playerz) {
        boolean betweenX = ((playerx < x1 && playerx > x2)
                || (playerx > x1 && playerx < x2));
        boolean betweenZ = ((playerz < z1 && playerz > z2)
                || (playerz > z1 && playerz < z2));
        return betweenX && betweenZ;
    }

    /**
     * Checks if the provided location pair is inside this location
     * 
     * @param playerLocation The player's current location
     * @return True if inside, false otherwise
     */
    public boolean IsBetween(LocationPair playerLocation) {
        return IsBetween(playerLocation.x, playerLocation.z);
    }
}
