package pw.spannercraft.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import pw.spannercraft.PrivateWarps;
import pw.spannercraft.WarpManager;  // Cambio aquí: importar desde el paquete correcto
import net.luckperms.api.LuckPermsProvider;

import java.util.ArrayList;
import java.util.List;

public class PWarpCommand implements CommandExecutor, TabCompleter {

    private WarpManager warpManager;
    private PrivateWarps plugin;
    private LuckPerms luckPermsAPI;

    public PWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;

        // Obtener la instancia de LuckPerms de manera segura
        LuckPerms luckPerms = LuckPermsProvider.get();  // Usamos LuckPermsProvider.get() para obtener la instancia
        if (luckPerms != null) {
            this.luckPermsAPI = luckPerms;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.onlyPlayers"));
            return false;
        }

        Player player = (Player) sender;

        // Obtener el grupo del jugador usando LuckPerms
        String group = "default"; // Valor por defecto si no se encuentra el grupo
        if (luckPermsAPI != null) {
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                group = user.getPrimaryGroup();
            }
        }

        // Obtener el límite de warps para el grupo
        int limit = plugin.getConfig().getInt("warpLimits." + group, 5); // Límite por defecto 5 si no está configurado
        int currentWarpsCount = warpManager.getPlayerWarps(player.getUniqueId()).size();

        if (args.length == 0) {
            // Mostrar los warps del jugador
            if (currentWarpsCount == 0) {
                player.sendMessage(plugin.getConfig().getString("messages.noWarps"));
            } else {
                player.sendMessage(plugin.getConfig().getString("messages.warpsList"));
                warpManager.getPlayerWarps(player.getUniqueId()).forEach((name, location) -> {
                    player.sendMessage(name + " - " + location.getWorld().getName() + " [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]");
                });
                player.sendMessage(plugin.getConfig().getString("messages.currentWarpCount")
                        .replace("%count%", String.valueOf(currentWarpsCount))
                        .replace("%limit%", String.valueOf(limit)));
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
            player.sendMessage(plugin.getConfig().getString("messages.teleportSuccess")
                    .replace("%warpName%", warpName));
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
