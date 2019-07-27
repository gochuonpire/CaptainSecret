package me.captain.secret;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author captainawesome7
 */
public class Waiting {
    
    private final CaptainSecret plugin;
    
    private final Player player;
    private final Integer timer;
    private Material mat;
    private Block block;
    private boolean removing;
    private boolean under;
    
    public Waiting(CaptainSecret instance, Player player, Integer time) {
        this.plugin = instance;
        this.player = player;
        this.timer = time;
        this.removing = false;
        this.under = false;
        this.block = null;
    }
    
    // Set the block for a new secret, add it to list
    public void setBlock(Block b, Material material) {
        block = b;
        this.mat = material;
        plugin.waiting.remove(player);
        plugin.secrets.put(block, new Secret(plugin, block.getLocation(), timer, this.mat));
        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Added secret block");
    }

    /**
     * @return If the player did /secret remove
     */
    public boolean isRemoving() {
        return removing;
    }

    /**
     * @param removing set removing
     */
    public void setRemoving(boolean removing) {
        this.removing = removing;
    }

    /**
     * @return If the player did /secret change
     */
    public boolean isUnder() {
        return under;
    }

    /**
     * @param under set under
     */
    public void setUnder(boolean under) {
        this.under = under;
    }
    
    // Store the secret block for changing redstone block here
    public void setUnderStorage(Block b) {
        this.block = b;
    }
    // Get the secret block to retrieve actual secret for editing
    public Block getBlock() {
        return this.block;
    }
}
