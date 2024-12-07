package pw.spannercraft;

import org.bukkit.plugin.java.JavaPlugin;
import pw.spannercraft.commands.SetPWarpCommand;
import pw.spannercraft.commands.DelPWarpCommand;
import pw.spannercraft.commands.PWarpCommand;
import pw.spannercraft.commands.PWarpReloadCommand;
import pw.spannercraft.managers.WarpManager;

public class PrivateWarps extends JavaPlugin {

    private WarpManager warpManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.warpManager = new WarpManager(this);

        // Registra los comandos implementando CommandExecutor
        getCommand("setpwarp").setExecutor(new SetPWarpCommand(warpManager, this));
        getCommand("delpwarp").setExecutor(new DelPWarpCommand(warpManager, this));
        getCommand("pwarp").setExecutor(new PWarpCommand(warpManager, this));
        getCommand("pwarpreload").setExecutor(new PWarpReloadCommand(this));

        // Asocia el TabCompleter solo a los comandos que lo necesiten
        getCommand("delpwarp").setTabCompleter(new DelPWarpCommand(warpManager, this));
        getCommand("pwarp").setTabCompleter(new PWarpCommand(warpManager, this));

        // Cargar warps desde el archivo pwarps.yml
        warpManager.loadWarps();
    }

    @Override
    public void onDisable() {
        warpManager.saveWarps();
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }
}
