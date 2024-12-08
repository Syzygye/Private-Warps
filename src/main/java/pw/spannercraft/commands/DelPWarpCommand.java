package pw.spannercraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import pw.spannercraft.PrivateWarps;
import pw.spannercraft.WarpManager;  // Cambié esta línea para importar desde el paquete correcto
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.LuckPermsProvider;

import java.util.ArrayList;
import java.util.List;

public class DelPWarpCommand implements CommandExecutor, TabCompleter {

    private WarpManager warpManager;
    private PrivateWarps plugin;
    private LuckPerms luckPermsAPI;

    public DelPWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;

        // Obtener la instancia de LuckPerms de manera segura
        LuckPerms luckPerms = LuckPermsProvider.get();  // Usamos LuckPermsProvider.get() de forma segura
        if (luckPerms != null) {
            this.luckPermsAPI = luckPerms;
        }
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

        // Eliminar el warp
        warpManager.removeWarp(player.getUniqueId(), warpName);

        // Enviar mensaje de éxito
        player.sendMessage(plugin.getConfig().getString("messages.warpDeleted")
                .replace("%warpName%", warpName));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            // Completado automático basado en los warps del jugador
            StringUtil.copyPartialMatches(args[0], warpManager.getPlayerWarps(((Player) sender).getUniqueId()).keySet(), suggestions);
        }
        return suggestions;
    }
}
