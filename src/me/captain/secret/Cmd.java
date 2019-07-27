package me.captain.secret;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author captainawesome7
 */
public class Cmd implements CommandExecutor {

    private final CaptainSecret plugin;

    public Cmd(CaptainSecret instance) {
        plugin = instance;
    }

    // Method runs on every command execution of /secret, returns command success
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (args[0].equals("new")) {
                if (player.hasPermission("captainsecret.*") || player.hasPermission("captainsecret.new")) {
                    // Create a new secret with the specified timer, wait for left clicks
                    Integer i = Integer.parseInt(args[1]);
                    plugin.waiting.put(player, new Waiting(plugin, player, i));
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Waiting for new secret block...");
                    return true;
                }
            } else if (args[0].equals("remove")) {
                if (player.hasPermission("captainsecret.*") || player.hasPermission("captainsecret.remove")) {
                    // Remove the secret designated by the next left click
                    plugin.waiting.put(player, new Waiting(plugin, player, 0));
                    plugin.waiting.get(player).setRemoving(true);
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Left click the block you wish to remove");
                }
            } else if (args[0].equals("change")) {
                if (player.hasPermission("captainsecret.*") || player.hasPermission("captainsecret.change")) {
                    // Change where the redstone block is, wait for secret left click
                    Waiting w = new Waiting(plugin, player, 0);
                    w.setUnder(true);
                    plugin.waiting.put(player, w);
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "CaptainSecret" + ChatColor.GRAY + "]: Left click the secret to change redstone");
                }
            } else if (args[0].equals("list")) {
                if (player.hasPermission("captainsecret.*") || player.hasPermission("captainsecret.list")) {
                    // List all the secrets
                    String msg = ChatColor.GRAY + "[";
                    Integer i = 0;
                    for(Secret s : plugin.secrets.values()) {
                        msg += ChatColor.GREEN + "secret" + i.toString() + ChatColor.GRAY + ",";
                        i++;
                    }
                    msg = msg.substring(0, msg.length());
                    msg += "]";
                    player.sendMessage(ChatColor.GRAY + "Current Secrets: ");
                    player.sendMessage(msg);
                }
            } else if(args[0].equals("info")) {
                // Display info concerning the specified secret
                Integer i = Integer.parseInt(args[1]);
                Secret s = (Secret) plugin.secrets.values().toArray()[i];
                String bmsg = ChatColor.GREEN + "Block " + ChatColor.GRAY;
                Location loc = s.getBlock();
                String bx = Integer.toString(loc.getBlockX());
                String by = Integer.toString(loc.getBlockY());
                String bz = Integer.toString(loc.getBlockZ());
                bmsg += "x:" + bx + " y:" + by + " z:" + bz;
                String wmsg = ChatColor.GREEN + "World " + ChatColor.GRAY + loc.getWorld().getName();
                String umsg = ChatColor.GREEN + "Under " + ChatColor.GRAY;
                Location uloc = s.getUnder();
                String ux = Integer.toString(uloc.getBlockX());
                String uy = Integer.toString(uloc.getBlockY());
                String uz = Integer.toString(uloc.getBlockZ());
                umsg += "x:" + ux + " y:" + uy + " z:" + uz;
                String tmsg = ChatColor.GREEN + "Timer " + ChatColor.GRAY + Integer.toString(s.getTime()) + " ticks";
                String mmsg = ChatColor.GREEN + "Material " + ChatColor.GRAY + s.mat.toString();
                player.sendMessage(wmsg);
                player.sendMessage(bmsg);
                player.sendMessage(umsg);
                player.sendMessage(tmsg);
                player.sendMessage(mmsg);
            }
        }
        return false;
    }
}
