package pw.spannercraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import pw.spannercraft.PrivateWarps;
import pw.spannercraft.managers.WarpManager;

import java.util.ArrayList;
import java.util.List;

public class DelPWarpCommand implements CommandExecutor, TabCompleter {

    private WarpManager warpManager;
    private PrivateWarps plugin;

    public DelPWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("¡Este comando solo puede ser ejecutado por un jugador!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("¡Debes proporcionar el nombre del warp para eliminarlo!");
            return false;
        }

        String warpName = args[0];
        if (!warpManager.getPlayerWarps(player.getUniqueId()).containsKey(warpName)) {
            player.sendMessage(plugin.getConfig().getString("messages.warpNotFound"));
            return false;
        }

        warpManager.removeWarp(player.getUniqueId(), warpName);
        player.sendMessage("Warp " + warpName + " eliminado.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], warpManager.getPlayerWarps(((Player) sender).getUniqueId()).keySet(), suggestions);
        }
        return suggestions;
    }
}
