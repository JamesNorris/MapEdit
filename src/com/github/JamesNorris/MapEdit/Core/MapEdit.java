/**
 * MapEdit
 * Copyright (C) 2012 Jnorr44
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.JamesNorris.MapEdit.Core;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.JamesNorris.MapEdit.Visual.Rendering;

public class MapEdit extends JavaPlugin {
	public static boolean a, c;
	public static String d;
	public static int e, g, j, l, m;
	public short n;
	public ArrayList<String> plugins = new ArrayList<String>();
	public static Logger log = Logger.getLogger("Minecraft");
	public String s1, s2, s3, s4;

	@Override public void onEnable() {

		/**
		 * Get the config.yml from the .jar
		 */

		final File f = new File(getDataFolder(), "config.yml");
		if (!f.exists()) {
			saveDefaultConfig();
		}

		/**
		 * Load all preferences from the config.yml
		 */

		if (f.exists()) {
			System.out.println("[MapEdit] Loading preferences...");
			a = getConfig().getBoolean("Track-Players");
			c = getConfig().getBoolean("Debug");
			d = getConfig().getString("Default-Player-Color");
			j = getConfig().getInt("Player-Ping");

			/**
			 * Load the strings
			 */

			s1 = "Track-Players: " + a;
			s2 = "Default-Player-Color: " + d;
			s3 = "Player-Ping: " + j;
			s4 = "Debug: " + c;

			/**
			 * Send preferences to the console if Debug is true
			 */

			if (c) {
				log.info("[MapEdit] [Debug] " + s1);
				log.info("[MapEdit] [Debug] " + s2);
				log.info("[MapEdit] [Debug] " + s3);
				log.info("[MapEdit] [Debug] " + s4);
			}
		}

		/**
		 * Add to the list of plugins
		 */

		if (c) {
			final Plugin[] pls = Bukkit.getServer().getPluginManager().getPlugins();
			for (final Plugin p1 : pls) {
				plugins.add(p1.toString());
			}
		}


		/**
		 * Register all other classes
		 */

		getServer().getPluginManager().registerEvents(new Events(this), this);
		new Rendering(this);

		/**
		 * Enable message
		 */

		System.out.println("[MapEdit] MapEdit has been enabled!");
	}

	@Override public void onDisable() {
		Events.registered.clear();

		/**
		 * Print out debug information
		 */

		if (c) {
			System.out.println("---------------[MapEdit] [Debug]---------------");
			System.out.println("If there is a bug, post this as a ticket:");
			System.out.println(s1);
			System.out.println(s2);
			System.out.println(s3);
			System.out.println(s4);
			System.out.println("MapEdit render number: " + e);
			System.out.println("MapView registration attempts: " + g);
			System.out.println("Server IP: " + Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort());
			System.out.println("Connection throttle: " + Bukkit.getServer().getConnectionThrottle());
			System.out.println("Bukkit version: " + Bukkit.getServer().getBukkitVersion().toString());
			System.out.println("Plugins running: [" + Bukkit.getServer().getPluginManager().getPlugins().length + "]");
			System.out.println(plugins);
			System.out.println("---------------[MapEdit] [Debug]---------------");
		}

		/**
		 * Disable message
		 */

		System.out.println("[MapEdit] MapEdit has been disabled!");
	}
}
