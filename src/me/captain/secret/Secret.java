package me.captain.secret;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author captainawesome7
 */
class Secret {

    private final Location location;
    private Location under;
    private final Integer time;
    public int power;
    public Material mat;
    public boolean activated;
    public CaptainSecret plugin;

    public Secret(CaptainSecret instance, Location loc, Integer timer, Material material) {
        plugin = instance;
        location = loc;
        under = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());
        time = timer;
        power = loc.getBlock().getBlockPower();
        mat = material;
    }

    // Runs every time the secret block is right clicked
    public void Activate() {
        if (activated && time > 0) {
            // if the timer is running (already clicked), don't do anything
        } else if (time > 0) {
            // if the timer isn' trunning, lets start it
            power = 15;
            under.getBlock().setType(Material.REDSTONE_BLOCK);
            activated = true;
            Runnable rs = () -> {
                    power = 0;
                    under.getBlock().setType(Material.AIR);
                    activated = false;
            };
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, rs, time);
        } else {
            // if there is no timer, just toggle the block redstone/air
            if (power > 0) {
                power = 0;
                under.getBlock().setType(Material.AIR);
                activated = false;
            } else {
                power = 15;
                under.getBlock().setType(Material.REDSTONE_BLOCK);
                activated = true;
            }
        }
    }
    
    // Set the under (redstone) block location
    public void setUnder(Location loc) {
        this.under = loc;
    }
    // Get the under (redstone) block position
    public Location getUnder() {
        return under;
    }
    // Get the timer (in ticks)
    public int getTime() {
        return time;
    }
    // Get the secret block location
    public Location getBlock() {
        return location;
    }
}
