package com.pg85.otg.bukkit.commands;

import com.pg85.otg.OTG;
import com.pg85.otg.bukkit.OTGPlugin;
import com.pg85.otg.bukkit.world.WorldHelper;
import com.pg85.otg.common.LocalWorld;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.List;

public abstract class BaseCommand
{
    public static final String ERROR_COLOR = ChatColor.RED.toString();
    public static final String MESSAGE_COLOR = ChatColor.GREEN.toString();
    static final String VALUE_COLOR = ChatColor.DARK_GREEN.toString();
	
    String name;
    String perm;
    String usage;
    protected OTGPlugin plugin;

    BaseCommand(OTGPlugin _plugin)
    {
        this.plugin = _plugin;
    }

    public abstract boolean onCommand(CommandSender sender, List<String> args);

    /**
     * Gets the {@link LocalWorld} the sender has provided. If the sender
     * provided an empty string, the current world of the sender is returned.
     * This may be null if the sender is not in a world loaded by Open Terrain
     * Generator. If the sender provided a non-empty string, but Open Terrain Generator
     * has no world loaded with that name, null will be returned.
     * 
     * @param sender
     *            The sender.
     * @param worldName
     *            The world name the sender provided. May be empty.
     * @return The world, or null if not found.
     */
    protected LocalWorld getWorld(CommandSender sender, String worldName)
    {
        if (worldName.isEmpty())
        {
            Location location = getLocation(sender);
            if (location != null)
            {
                LocalWorld world = WorldHelper.toLocalWorld(location.getWorld());
                if (world != null)
                {
                    return world;
                }
            }
        }

        return OTG.getWorld(worldName);
    }

    /**
     * If the sender has a location (it is a player or a command block), this
     * method returns their location. If not, this method returns null.
     * 
     * @param sender
     *            The sender.
     * @return The location, or null if not found.
     */
    protected Location getLocation(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return ((Player) sender).getLocation();
        }

        if (sender instanceof BlockCommandSender)
        {
            return ((BlockCommandSender) sender).getBlock().getLocation();
        }

        return null;
    }

    protected void listMessage(CommandSender sender, List<String> lines, int page, String... headers)
    {
        int pageCount = (lines.size() >> 3) + 1;
        if (page > pageCount)
        {
            page = pageCount;
        }

        sender.sendMessage(ChatColor.AQUA.toString() + headers[0] + " - page " + page + "/" + pageCount);
        for (int headerId = 1; headerId < headers.length; headerId++)
        {
            // Send all remaining headers
            sender.sendMessage(ChatColor.AQUA + headers[headerId]);
        }

        page--;

        for (int i = page * 8; i < lines.size() && i < (page * 8 + 8); i++)
        {
            sender.sendMessage(lines.get(i));
        }
    }

    public String getHelp()
    {
        String ret = "do that";
        Permission permission = Bukkit.getPluginManager().getPermission(perm);
        if (permission != null)
        {
            String desc = permission.getDescription();
            if (desc != null && desc.trim().length() > 0)
            {
                ret = desc.trim();
            }
        }
        return ret;
    }
}