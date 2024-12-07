package pw.spannercraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pw.spannercraft.PrivateWarps;

public class PWarpReloadCommand implements CommandExecutor {

    private PrivateWarps plugin;

    public PWarpReloadCommand(PrivateWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();  // Recarga la configuración
        sender.sendMessage(plugin.getConfig().getString("messages.configReloaded"));
        return true;
    }
}
