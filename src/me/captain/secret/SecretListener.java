package me.captain.secret;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author captainawesome7
 */
public class SecretListener implements Listener {
    
    private final CaptainSecret plugin;

    public SecretListener(CaptainSecret instance) {
        this.plugin = instance;
    }
    
    // Event triggers every time a player right/left clicks in game
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        // If the player is executing commands (adding/changing/removing a secret)
        if(plugin.waiting.containsKey(player)) {
            Block b = e.getClickedBlock();
            // fuck a deprecation
            Material m = player.getItemInHand().getType();
            Waiting w = plugin.waiting.get(player);
            if(w.isRemoving()) {
                // If the player did /secret remove, lets delete it
                if(plugin.secrets.containsKey(b)) {
                    plugin.secrets.remove(b);
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Deleted secret block");
                    plugin.waiting.remove(player);
                } else {
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: That is not a secret block, /secret cancel to cancel");
                }
            } else if(w.isUnder()) {
                // If player did /secret change, lets change the under (redstone) block
                // If they havent chosen the secret block, set it now
                if(w.getBlock() == null) {
                    if(plugin.secrets.containsKey(b)) {
                        w.setUnderStorage(b);
                        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Select the new redstone block");
                    } else {
                        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: That is not a secret block, /secret cancel to cancel");
                    }
                } else {
                    // If they have chosen the secret block, set the new under (redstone block)
                    Secret s = plugin.secrets.get(w.getBlock());
                    s.setUnder(b.getLocation());
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Secret redstone block changed");
                    plugin.waiting.remove(player);
                }
            } else {
                w.setBlock(b, m);
            }
        } else {
            // If the player is just clicking random shit, see if its a secret
            Block b = e.getClickedBlock();
            if(plugin.secrets.containsKey(b)) {
                if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Secret s = plugin.secrets.get(b);
                    // If the item matches, activate the block. Cancel the event. Fuck a deprecation i dont care
                    if(player.getItemInHand().getType() == s.mat) {
                        s.Activate();
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
