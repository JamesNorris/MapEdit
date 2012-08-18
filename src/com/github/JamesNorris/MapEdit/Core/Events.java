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

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.github.JamesNorris.MapEdit.Visual.Rendering;

public class Events implements Listener {

	public static ArrayList<String> registered = new ArrayList<String>();

	private final MapEdit plugin;

	public Events(final MapEdit instance) {
		plugin = instance;
	}

	/**
	 * Finds the players held item, and if it is a map, register a view for that durability
	 * 
	 * @param event
	 */

	@EventHandler public void PIHE(final PlayerItemHeldEvent event) {
		final ItemStack e = event.getPlayer().getItemInHand();

		/**
		 * If the item in hand is a map, and the player has not yet been registered...
		 */

		if (e.getType() == Material.MAP && !registered.contains(event.getPlayer().getName())) {
			final short durability = e.getDurability();
			final MapView m = Bukkit.getServer().getMap(durability);

			/**
			 * If the mapview exists
			 */

			if (m != null) {

				/**
				 * Clear its renderers, and add the custom renderer
				 */

				m.getRenderers().clear();
				final MapRenderer mr = new Rendering(plugin);
				m.addRenderer(mr);
				registered.add(event.getPlayer().getName());

				/**
				 * Print out the debug string
				 */

				if (MapEdit.c) {
					MapEdit.log.info("[MapEdit] [Debug] Registered a MapView.");
					MapEdit.g++;
				}
			}
		}
	}
}
