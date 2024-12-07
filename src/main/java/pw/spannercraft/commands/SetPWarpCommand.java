package pw.spannercraft.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.spannercraft.PrivateWarps;
import pw.spannercraft.managers.WarpManager;

public class SetPWarpCommand implements CommandExecutor {

    private WarpManager warpManager;
    private PrivateWarps plugin;

    public SetPWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
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
            player.sendMessage(plugin.getConfig().getString("messages.setpwarpUsage"));
            return false;
        }

        String warpName = args[0];
        Location location = player.getLocation(); // Obtener la ubicación actual del jugador

        // Verificar si el jugador tiene el límite de warps
        int limit = plugin.getConfig().getInt("settings.maxWarps", 5);  // Acceder a maxWarps dentro de 'settings'
        boolean warpExists = warpManager.getPlayerWarps(player.getUniqueId()).containsKey(warpName);

        // Si el warp no existe y el jugador ha alcanzado el límite de warps
        if (!warpExists && warpManager.getPlayerWarps(player.getUniqueId()).size() >= limit) {
            player.sendMessage(plugin.getConfig().getString("messages.maxWarpsExceeded"));
            return false;
        }

        // Si el warp ya existe, lo sobrescribimos sin afectar el límite
        warpManager.addWarp(player.getUniqueId(), warpName, location);
        player.sendMessage(plugin.getConfig().getString("messages.warpSaved"));
        return true;
    }
}
