package pw.spannercraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpManager {
    private final Map<UUID, Map<String, Location>> playerWarps = new HashMap<>();
    private final PrivateWarps plugin;
    private final File warpsFile;

    public WarpManager(PrivateWarps plugin) {
        this.plugin = plugin;
        this.warpsFile = new File(plugin.getDataFolder(), "pwarps.yml");

        // Cargar los warps al inicio
        loadWarps();
    }

    public void addWarp(UUID playerId, String warpName, Location location) {
        playerWarps.computeIfAbsent(playerId, k -> new HashMap<>()).put(warpName, location);
        saveWarps(); // Guardado inmediato tras añadir un warp
    }

    public void removeWarp(UUID playerId, String warpName) {
        Map<String, Location> warps = playerWarps.get(playerId);
        if (warps != null) {
            warps.remove(warpName);
            saveWarps(); // Guardado inmediato tras eliminar un warp
        }
    }

    public Map<String, Location> getPlayerWarps(UUID playerId) {
        return playerWarps.getOrDefault(playerId, new HashMap<>());
    }

    public void loadWarps() {
        if (!warpsFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);

        if (config.contains("warps")) {
            for (String playerIdString : config.getConfigurationSection("warps").getKeys(false)) {
                UUID playerId = UUID.fromString(playerIdString);
                Map<String, Location> warps = new HashMap<>();

                for (String warpName : config.getConfigurationSection("warps." + playerIdString).getKeys(false)) {
                    String path = "warps." + playerIdString + "." + warpName;
                    String worldName = config.getString(path + ".world");
                    double x = config.getDouble(path + ".x");
                    double y = config.getDouble(path + ".y");
                    double z = config.getDouble(path + ".z");
                    float yaw = (float) config.getDouble(path + ".yaw");
                    float pitch = (float) config.getDouble(path + ".pitch");

                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                    warps.put(warpName, location);
                }

                playerWarps.put(playerId, warps);
            }
        }
    }

    public void saveWarps() {
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, Map<String, Location>> entry : playerWarps.entrySet()) {
            UUID playerId = entry.getKey();
            Map<String, Location> warps = entry.getValue();

            for (Map.Entry<String, Location> warpEntry : warps.entrySet()) {
                String warpName = warpEntry.getKey();
                Location location = warpEntry.getValue();

                String path = "warps." + playerId + "." + warpName;
                config.set(path + ".world", location.getWorld().getName());
                config.set(path + ".x", location.getX());
                config.set(path + ".y", location.getY());
                config.set(path + ".z", location.getZ());
                config.set(path + ".yaw", location.getYaw());
                config.set(path + ".pitch", location.getPitch());
            }
        }

        try {
            config.save(warpsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("No se pudo guardar el archivo pwarps.yml: " + e.getMessage());
        }
    }
}
