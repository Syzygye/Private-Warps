package pw.spannercraft;

import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import pw.spannercraft.commands.SetPWarpCommand;
import pw.spannercraft.commands.DelPWarpCommand;
import pw.spannercraft.commands.PWarpCommand;
import pw.spannercraft.commands.PWarpReloadCommand;
import net.luckperms.api.LuckPermsProvider; // Asegúrate de importar esta clase
import java.io.File;

public class PrivateWarps extends JavaPlugin {

    private WarpManager warpManager;
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        // Eliminar la creación de la subcarpeta PrivateWarps, ya no es necesario
        // Solo crear el archivo config.yml en la carpeta del plugin
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", true);  // Guardar el archivo config.yml en la carpeta del plugin
        }

        // Inicialización de WarpManager con el plugin como argumento
        this.warpManager = new WarpManager(this);  // Pasa 'this' para inicializar correctamente

        // Verificar si LuckPerms está instalado y obtener la API
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPermsProvider.get();  // Usar LuckPermsProvider.get() de manera segura
        } else {
            getLogger().warning("LuckPerms no está instalado. El sistema de límites de warps por grupo no funcionará.");
        }

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

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
