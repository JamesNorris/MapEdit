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

package com.github.JamesNorris.MapEdit.Visual;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.github.JamesNorris.MapEdit.Core.MapEdit;

public class Rendering extends MapRenderer {
	public Rendering(final MapEdit plugin) {}

	public ArrayList<MapCursor> mapcurs = new ArrayList<MapCursor>();
	public int a, b, f, C, d;
	public boolean armed;

	/**
	 * A custom map renderer, that changes the default map renderer
	 * 
	 * (render() is called once a second)
	 */

	@Override public void render(final MapView map, final MapCanvas canvas, final Player player) {

		/**
		 * Wait for the player ping
		 */

		++a;
		if (a >= MapEdit.j) {
			a = 0;

			final MapCursorCollection cur = canvas.getCursors();
			canvas.setCursors(cur);

			/**
			 * Removes cursors left over from the plugin restart
			 */

			if (armed == false) {
				for (int n = 0; n < cur.size(); n++) {
					final MapCursor mc = cur.getCursor(n);
					cur.removeCursor(mc);
				}
				armed = true;
			}

			/* REMOVAL OF OLD CHANGES */

			/**
			 * Removes placed cursors from the last render cycle
			 */

			if (!mapcurs.isEmpty()) {
				for (int j = 0; j < mapcurs.size(); j++) {
					final MapCursor mc = mapcurs.iterator().next();
					cur.removeCursor(mc);
					mapcurs.remove(mc);
				}
			}

			/**
			 * Find all players in the game
			 */

			final Player[] ps = Bukkit.getServer().getOnlinePlayers();
			for (final Player p2 : ps) {

				/* PERMISSIONS AND COLORS */

				if (!p2.hasPermission("mapedit.hidden") && p2 != player) {

					/**
					 * Changes the cursor color based on player permissions and config
					 */

					byte cursorcolor = 0;

					if (p2.hasPermission("mapedit.color.blue") || MapEdit.d.equalsIgnoreCase("blue")) {
						cursorcolor = 3;
					} else if (p2.hasPermission("mapedit.color.red") || MapEdit.d.equalsIgnoreCase("red")) {
						cursorcolor = 2;
					} else if (p2.hasPermission("mapedit.color.green") || MapEdit.d.equalsIgnoreCase("green")) {
						cursorcolor = 1;
					}

					/**
					 * Make sure the 2 players don't share a mapview, before placing a cursor
					 */

					short p2d = -1;
					final ItemStack it = p2.getItemInHand();
					if (it.getType() == Material.MAP) {
						p2d = it.getDurability();
					}

					if (Bukkit.getServer().getMap(p2d) != map) {

						/* ADDITION OF CURSORS */

						/**
						 * Get the center of the map
						 */

						final int centerX = map.getCenterX();
						final int centerZ = map.getCenterZ();

						/**
						 * Get the scale of the map, and keep it between 0 and 4
						 */

						int scale = map.getScale().getValue();

						if (scale < 0) {
							scale = 0;
						}

						if (scale > 4) {
							scale = 4;
						}

						/**
						 * Find the edges of the map
						 * 
						 * Now start the equations you will not understand...
						 */

						final float f = (float) (p2.getLocation().getX() - centerX) / (1 << scale);
						final float f1 = (float) (p2.getLocation().getZ() - centerZ) / (1 << scale);
						final byte b0 = 64;
						final byte b1 = 64;

						/**
						 * If the cursor is on the map...
						 */

						if (f >= (-b0) && f1 >= (-b1) && f <= b0 && f1 <= b1) {
							final byte b2 = cursorcolor;

							/**
							 * Get the x and y of the player on the map
							 */

							final byte b3 = (byte) ((int) ((f * 2.0F) + 0.5D));
							final byte b4 = (byte) ((int) ((f1 * 2.0F) + 0.5D));

							/**
							 * Get the player yaw, and convert it to an int 0 - 15
							 * 
							 * @UNKNOWN_BUG
							 *              When rotating at a certain angle, a blue cursor turns green,
							 *              however, a print says it is still blue.
							 *              This must be a Bukkit/Minecraft bug
							 */

							final int dir = (int) (p2.getLocation().getYaw() * 16.0D / 360.0D);

							/**
							 * If b5 is less than 0, change it to the positive of the same location
							 * NORTH = 8
							 * 
							 * @UNKNOWN_BUG
							 *              When rotating to 8, the cursor actually points to 7.
							 */

							C = dir & 0xF;

							/**
							 * Add the cursor
							 */

							final MapCursor mc = new MapCursor(b3, b4, (byte) C, b2, true);
							cur.addCursor(mc);
							mapcurs.add(mc);

						}

					}
				}
			}
			++MapEdit.e;
		}
	}
}
