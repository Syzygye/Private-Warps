package pw.spannercraft.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.spannercraft.PrivateWarps;
import pw.spannercraft.WarpManager;  // Asegúrate de importar WarpManager correctamente
import net.luckperms.api.LuckPermsProvider; // Asegúrate de importar esta clase

public class SetPWarpCommand implements CommandExecutor {

    private WarpManager warpManager;
    private PrivateWarps plugin;
    private LuckPerms luckPerms;

    public SetPWarpCommand(WarpManager warpManager, PrivateWarps plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get(); // Usamos LuckPermsProvider.get() directamente
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

        // Obtener el grupo del jugador usando LuckPerms
        String group = "default"; // Valor por defecto si no se encuentra el grupo
        if (luckPerms != null) {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                group = user.getPrimaryGroup();
            }
        }

        // Obtener el límite de warps para el grupo
        int limit = plugin.getConfig().getInt("warpLimits." + group, 5); // Límite por defecto 5 si no está configurado

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
