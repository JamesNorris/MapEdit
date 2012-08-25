package com.github.JamesNorris.MapEdit.Visual;

import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.github.JamesNorris.MapEdit.Core.MapEdit;

public class MapScale {

	/**
	 * Sets the map scale to the default scale of the server.
	 * 
	 * @param map
	 */

	public static void setScale(MapView map) {
		int s = MapEdit.q;
		Scale scale = Scale.NORMAL;
		switch (s) {
			case 1:
				scale = Scale.CLOSEST;
			break;
			case 2:
				scale = Scale.CLOSE;
			break;
			case 3:
				scale = Scale.NORMAL;
			break;
			case 4:
				scale = Scale.FAR;
			break;
			case 5:
				scale = Scale.FARTHEST;
			break;
		}
		if (map.getScale() != scale) {
			map.setScale(scale);
		}
	}
}
