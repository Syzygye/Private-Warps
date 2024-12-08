package pw.spannercraft;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpManager {

    private Map<UUID, Map<String, Location>> playerWarps;
    private PrivateWarps plugin;
    private File warpsFile;

    // Constructor modificado para aceptar PrivateWarps y configurar el archivo pwarps.yml
    public WarpManager(PrivateWarps plugin) {
        this.plugin = plugin;
        this.playerWarps = new HashMap<>();
        this.warpsFile = new File(plugin.getDataFolder(), "pwarps.yml");

        // Cargar los warps al iniciar el plugin
        loadWarps();

        // Programar la tarea para guardar los warps cada 5 minutos
        Bukkit.getScheduler().runTaskTimer(plugin, this::saveWarps, 0L, 1000L * 60 * 5);

        // Guardar los warps cuando un jugador se desconecta
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
                saveWarps();
            }
        }, plugin);
    }

    public void addWarp(UUID playerId, String warpName, Location location) {
        playerWarps
                .computeIfAbsent(playerId, k -> new HashMap<>())
                .put(warpName, location);
    }

    public void removeWarp(UUID playerId, String warpName) {
        Map<String, Location> warps = playerWarps.get(playerId);
        if (warps != null) {
            warps.remove(warpName);
        }
    }

    public Map<String, Location> getPlayerWarps(UUID playerId) {
        return playerWarps.getOrDefault(playerId, new HashMap<>());
    }

    public void loadWarps() {
        if (!warpsFile.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);

        for (String playerIdString : config.getConfigurationSection("warps").getKeys(false)) {
            UUID playerId = UUID.fromString(playerIdString);
            Map<String, Location> warps = new HashMap<>();

            for (String warpName : config.getConfigurationSection("warps." + playerId).getKeys(false)) {
                String worldName = config.getString("warps." + playerId + "." + warpName + ".world");
                double x = config.getDouble("warps." + playerId + "." + warpName + ".x");
                double y = config.getDouble("warps." + playerId + "." + warpName + ".y");
                double z = config.getDouble("warps." + playerId + "." + warpName + ".z");
                float yaw = (float) config.getDouble("warps." + playerId + "." + warpName + ".yaw");
                float pitch = (float) config.getDouble("warps." + playerId + "." + warpName + ".pitch");

                Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                warps.put(warpName, location);
            }

            playerWarps.put(playerId, warps);
        }
    }

    public void saveWarps() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);

        // Iterar sobre todos los warps en memoria y agregarlos a la configuración
        for (Map.Entry<UUID, Map<String, Location>> entry : playerWarps.entrySet()) {
            UUID playerId = entry.getKey();
            Map<String, Location> warps = entry.getValue();

            for (Map.Entry<String, Location> warpEntry : warps.entrySet()) {
                String warpName = warpEntry.getKey();
                Location location = warpEntry.getValue();

                config.set("warps." + playerId + "." + warpName + ".world", location.getWorld().getName());
                config.set("warps." + playerId + "." + warpName + ".x", location.getX());
                config.set("warps." + playerId + "." + warpName + ".y", location.getY());
                config.set("warps." + playerId + "." + warpName + ".z", location.getZ());
                config.set("warps." + playerId + "." + warpName + ".yaw", location.getYaw());
                config.set("warps." + playerId + "." + warpName + ".pitch", location.getPitch());
            }
        }

        // Intentar guardar el archivo pwarps.yml
        try {
            config.save(warpsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
