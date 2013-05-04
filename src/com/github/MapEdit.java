package com.github;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MapEdit extends JavaPlugin {
    public static boolean shouldTrackPlayers, DEBUG, startingMap, shouldMoveMap, socialMode;
    public static String defaultColor;
    public static int renderCount, registrationCount, renderDelay, l, m, defaultScale;
    public short n;
    public ArrayList<String> plugins = new ArrayList<String>();
    public static Logger log = Logger.getLogger("Minecraft");

    @Override public void onEnable() {
        /* Get the config.yml from the .jar*/
        final File f = new File(getDataFolder(), "config.yml");
        if (!f.exists()) {
            saveDefaultConfig();
        }
        /* Load all preferences from the config.yml */
        if (f.exists()) {
            System.out.println("[MapEdit] Loading preferences...");
            shouldTrackPlayers = getConfig().getBoolean("Track-Players");
            DEBUG = getConfig().getBoolean("Debug");
            defaultColor = getConfig().getString("Default-Player-Color");
            renderDelay = getConfig().getInt("Player-Ping");
            startingMap = getConfig().getBoolean("Starting-Map");
            shouldMoveMap = getConfig().getBoolean("Move-Map");
            defaultScale = getConfig().getInt("Default-Scale");
            socialMode = getConfig().getBoolean("Social-Mode");
        }
        /* Add to the list of plugins */
        if (DEBUG) {
            final Plugin[] pls = Bukkit.getServer().getPluginManager().getPlugins();
            for (final Plugin p1 : pls) {
                plugins.add(p1.toString());
            }
        }
        /* Register all other classes */
        getServer().getPluginManager().registerEvents(new Events(), this);
        /* Enable message */
        System.out.println("[MapEdit] MapEdit has been enabled!");
    }

    @Override public void onDisable() {
        Events.registered.clear();
        /* Print out debug information */
        if (DEBUG) {
            System.out.println("---------------[MapEdit] [Debug]---------------");
            System.out.println("If there is a bug, post this as a ticket:");
            System.out.println("MapEdit render count: " + renderCount);
            System.out.println("MapView registrations: " + registrationCount);
            System.out.println("Server IP: " + Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort());
            System.out.println("Connection throttle: " + Bukkit.getServer().getConnectionThrottle());
            System.out.println("Bukkit version: " + Bukkit.getServer().getBukkitVersion().toString());
            System.out.println("Plugins running: [" + plugins.size() + "]");
            System.out.println(plugins);
            System.out.println("---------------[MapEdit] [Debug]---------------");
        }
        /* Disable message */
        System.out.println("[MapEdit] MapEdit has been disabled!");
    }
}
