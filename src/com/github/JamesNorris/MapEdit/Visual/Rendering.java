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

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.github.JamesNorris.MapEdit.Core.MapEdit;

public class Rendering extends MapRenderer {

	public static int a;
	public static HashMap<MapView, Player> maps = new HashMap<MapView, Player>();

	public Rendering(final MapEdit plugin) {}

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

			/**
			 * Renders player cursors on the map
			 */

			PlayerRendering.renderPlayerCursor(map, canvas, player);
		}
	}
}
