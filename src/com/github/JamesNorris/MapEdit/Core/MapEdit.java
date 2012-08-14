package com.github.JamesNorris.MapEdit.Core;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.JamesNorris.MapEdit.Visual.PlayerTracking;

public class MapEdit extends JavaPlugin {
	public static boolean a;
	public static ArrayList<String> players = new ArrayList<String>();
	public static int b;
	public static int updateRate;
	public int id;
	public MapView m;

	public void onEnable() {
		
		/**
		 * Register all other classes
		 */
		
		this.getServer().getPluginManager().registerEvents(new Events(), this);

		/**
		 * Load all config values/preferences, or get the config.yml from the .jar
		 */

		File f = new File(getDataFolder(), "config.yml");
		if (!f.exists()) {
			saveDefaultConfig();
		}
		if (f.exists()) {
			System.out.println("[MapEdit] Loading preferences...");
			a = this.getConfig().getBoolean("Track-Players");
		}

		/**
		 * Add players to the ArrayList players, then register all maps with the new, custom renderer
		 */

		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (!players.contains(p.getName())) {
						players.add(p.getName());
						for (int j = 0; j < p.getInventory().getSize(); j++) {
							ItemStack is = p.getInventory().getItem(j);
							if (is != null && is.getType() == Material.MAP) {
								short slot = p.getInventory().getItem(j).getDurability();
								MapView m = Bukkit.getMap(slot);
								if (m != null) {
									m.getRenderers().clear();
									m.addRenderer(new PlayerTracking(this));
									System.out.println("[MapEdit] Registered a Map!");
								}
							}
						}
					}
				}
			}
		}, 100, 100);

		// TODO make a list of worlds, and run a check every 5 minutes for a new world, if there is a new world, tell the op to reload the plugin

		/**
		 * Enable message
		 */

		System.out.println("[MapEdit] MapEdit has been enabled!");
	}

	public void onDisable() {

		/**
		 * Disable message
		 */

		System.out.println("[MapEdit] MapEdit has been disabled!");
	}
}
