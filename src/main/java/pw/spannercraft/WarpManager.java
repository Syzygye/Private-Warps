package pw.spannercraft.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pw.spannercraft.PrivateWarps;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpManager {

    private PrivateWarps plugin;
    private Map<UUID, Map<String, Location>> playerWarps = new HashMap<>();

    public WarpManager(PrivateWarps plugin) {
        this.plugin = plugin;
    }

    public void loadWarps() {
        File file = new File(plugin.getDataFolder(), "pwarps.yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String uuidString : config.getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                Map<String, Location> warps = new HashMap<>();
                for (String warpName : config.getConfigurationSection(uuidString).getKeys(false)) {
                    // Cargar la ubicación de cada warp
                    double x = config.getDouble(uuidString + "." + warpName + ".x");
                    double y = config.getDouble(uuidString + "." + warpName + ".y");
                    double z = config.getDouble(uuidString + "." + warpName + ".z");
                    float yaw = (float) config.getDouble(uuidString + "." + warpName + ".yaw");
                    float pitch = (float) config.getDouble(uuidString + "." + warpName + ".pitch");
                    String worldName = config.getString(uuidString + "." + warpName + ".world");

                    // Crear la ubicación
                    Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                    warps.put(warpName, loc);
                }
                playerWarps.put(uuid, warps);
            }
        }
    }

    public void saveWarps() {
        File file = new File(plugin.getDataFolder(), "pwarps.yml");
        YamlConfiguration config = new YamlConfiguration();

        // Guardar todos los warps de cada jugador
        for (UUID uuid : playerWarps.keySet()) {
            Map<String, Location> warps = playerWarps.get(uuid);
            for (String warpName : warps.keySet()) {
                Location loc = warps.get(warpName);
                config.set(uuid.toString() + "." + warpName + ".x", loc.getX());
                config.set(uuid.toString() + "." + warpName + ".y", loc.getY());
                config.set(uuid.toString() + "." + warpName + ".z", loc.getZ());
                config.set(uuid.toString() + "." + warpName + ".yaw", loc.getYaw());
                config.set(uuid.toString() + "." + warpName + ".pitch", loc.getPitch());
                config.set(uuid.toString() + "." + warpName + ".world", loc.getWorld().getName());
            }
        }

        // Guardar el archivo
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar los warps.");
        }
    }

    public Map<String, Location> getPlayerWarps(UUID uuid) {
        return playerWarps.getOrDefault(uuid, new HashMap<>());
    }

    public void addWarp(UUID uuid, String name, Location location) {
        Map<String, Location> warps = getPlayerWarps(uuid);
        warps.put(name, location);
        playerWarps.put(uuid, warps);
        saveWarps(); // Guardar los warps después de añadir uno nuevo
    }

    public void removeWarp(UUID uuid, String name) {
        Map<String, Location> warps = getPlayerWarps(uuid);
        if (warps != null && warps.containsKey(name)) {
            warps.remove(name);
            playerWarps.put(uuid, warps);
            saveWarps(); // Guardar los warps después de eliminar uno
        }
    }
}
