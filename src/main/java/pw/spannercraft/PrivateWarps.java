package pw.spannercraft;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pw.spannercraft.commands.SetPWarpCommand;
import pw.spannercraft.commands.DelPWarpCommand;
import pw.spannercraft.commands.PWarpCommand;
import pw.spannercraft.commands.PWarpReloadCommand;

import java.io.File;

public class PrivateWarps extends JavaPlugin {

    private WarpManager warpManager;
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", true);
        }

        this.warpManager = new WarpManager(this);

        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPermsProvider.get();
        } else {
            getLogger().warning("LuckPerms no está instalado. El sistema de límites de warps por grupo no funcionará.");
        }

        getCommand("setpwarp").setExecutor(new SetPWarpCommand(warpManager, this));
        getCommand("delpwarp").setExecutor(new DelPWarpCommand(warpManager, this));
        getCommand("pwarp").setExecutor(new PWarpCommand(warpManager, this));
        getCommand("pwarpreload").setExecutor(new PWarpReloadCommand(this));

        getCommand("delpwarp").setTabCompleter(new DelPWarpCommand(warpManager, this));
        getCommand("pwarp").setTabCompleter(new PWarpCommand(warpManager, this));

        warpManager.loadWarps();
    }

    @Override
    public void onDisable() {
        if (warpManager != null) {
            warpManager.saveWarps();
        } else {
            getLogger().warning("WarpManager no está inicializado. No se pueden guardar los warps.");
        }
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
