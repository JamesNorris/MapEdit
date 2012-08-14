package com.github.JamesNorris.MapEdit.Visual;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.github.JamesNorris.MapEdit.Core.MapEdit;

public class PlayerTracking extends MapRenderer {
	public PlayerTracking(Runnable runnable) {}

	public HashMap<Location, Byte> bytes = new HashMap<Location, Byte>();
	public int j;

	/**
	 * A custom map renderer, that changes the default map renderer
	 */

	@Override public void render(MapView map, MapCanvas canvas, Player player) {
		if (MapEdit.a) {

			/**
			 * Find all players in the game
			 */

			for (int k = 0; k < MapEdit.players.size(); k++) {
				String s = MapEdit.players.iterator().next();
				Player p2 = Bukkit.getServer().getPlayerExact(s);

				/**
				 * Removes old pixels
				 */

				if (!bytes.isEmpty()) {
					for (int i = 0; i < bytes.size(); i++) {
						Location l = bytes.keySet().iterator().next();
						Byte b = bytes.get(l);
						int x2 = l.getBlockX();
						int z2 = l.getBlockZ();
						canvas.setPixel(x2, z2, b);
						bytes.remove(l);
					}
				}

				if (!p2.hasPermission("mapedit.hidden")) {

					/**
					 * Changes the dot color based on player permissions
					 */

					int color = 48;

					if (p2.hasPermission("mapedit.color.blue")) {
						color = 48;
					}
					if (p2.hasPermission("mapedit.color.red")) {
						color = 16;
					}
					if (p2.hasPermission("mapedit.color.green")) {
						color = 4;
					}

					int x = p2.getLocation().getBlockX() - player.getLocation().getBlockX() + 64;
					int z = p2.getLocation().getBlockZ() - player.getLocation().getBlockZ() + 64;

					if (x >= 1 && x <= 126 && z >= 1 && z <= 126) {

						/**
						 * Saves all pixels to the bytes hashmap, so they can be retrieved later
						 */

						// 1
						Byte b1 = canvas.getPixel(x, z);
						Location l1 = player.getWorld().getBlockAt(x, 255, z).getLocation();
						bytes.put(l1, b1);

						// 2
						Byte b2 = canvas.getPixel(x - 1, z - 1);
						Location l2 = player.getWorld().getBlockAt(x - 1, 255, z - 1).getLocation();
						bytes.put(l2, b2);

						// 3
						Byte b3 = canvas.getPixel(x, z - 1);
						Location l3 = player.getWorld().getBlockAt(x, 255, z - 1).getLocation();
						bytes.put(l3, b3);

						// 4
						Byte b4 = canvas.getPixel(x + 1, z - 1);
						Location l4 = player.getWorld().getBlockAt(x + 1, 255, z - 1).getLocation();
						bytes.put(l4, b4);

						// 5
						Byte b5 = canvas.getPixel(x - 1, z);
						Location l5 = player.getWorld().getBlockAt(x - 1, 255, z).getLocation();
						bytes.put(l5, b5);

						// 6
						Byte b6 = canvas.getPixel(x + 1, z);
						Location l6 = player.getWorld().getBlockAt(x + 1, 255, z).getLocation();
						bytes.put(l6, b6);

						// 7
						Byte b7 = canvas.getPixel(x - 1, z + 1);
						Location l7 = player.getWorld().getBlockAt(x - 1, 255, z + 1).getLocation();
						bytes.put(l7, b7);

						// 8
						Byte b8 = canvas.getPixel(x, z + 1);
						Location l8 = player.getWorld().getBlockAt(x, 255, z + 1).getLocation();
						bytes.put(l8, b8);

						// 9
						Byte b9 = canvas.getPixel(x + 1, z + 1);
						Location l9 = player.getWorld().getBlockAt(x + 1, 255, z + 1).getLocation();
						bytes.put(l9, b9);

						/**
						 * Changes the pixel color
						 */

						canvas.setPixel(x, z, (byte) color);
						canvas.setPixel(x - 1, z - 1, (byte) color);
						canvas.setPixel(x, z - 1, (byte) color);
						canvas.setPixel(x + 1, z - 1, (byte) color);
						canvas.setPixel(x - 1, z, (byte) color);
						canvas.setPixel(x + 1, z, (byte) color);
						canvas.setPixel(x - 1, z + 1, (byte) color);
						canvas.setPixel(x, z + 1, (byte) color);
						canvas.setPixel(x + 1, z + 1, (byte) color);
					}
				}
			}
		}
	}
}
