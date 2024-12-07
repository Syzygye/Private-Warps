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

public class PWarpCommand implements CommandExecutor, TabCompleter {

    private WarpManager warpManager;
    private PrivateWarps plugin;

    public PWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
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
            // Obtener los warps del jugador
            if (warpManager.getPlayerWarps(player.getUniqueId()).isEmpty()) {
                // Si el jugador no tiene warps, enviar mensaje de aviso
                player.sendMessage(plugin.getConfig().getString("messages.noWarps"));
            } else {
                // Si el jugador tiene warps, mostrar la lista
                player.sendMessage(plugin.getConfig().getString("messages.warpsList"));
                warpManager.getPlayerWarps(player.getUniqueId()).forEach((name, location) -> {
                    player.sendMessage(name + " - " + location.getWorld().getName() + " [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]");
                });
            }
        } else {
            String warpName = args[0];
            if (!warpManager.getPlayerWarps(player.getUniqueId()).containsKey(warpName)) {
                // Si no se encuentra el warp, enviar mensaje de error
                player.sendMessage(plugin.getConfig().getString("messages.warpNotFound"));
                return false;
            }

            // Teletransportar al jugador al warp
            player.teleport(warpManager.getPlayerWarps(player.getUniqueId()).get(warpName));
            player.sendMessage(plugin.getConfig().getString("messages.teleportSuccess").replace("%warpName%", warpName));
        }
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
