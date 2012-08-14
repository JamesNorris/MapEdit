package com.github.JamesNorris.MapEdit.Core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
	@EventHandler
	public void PQU(PlayerQuitEvent event){
		Player p = event.getPlayer();
		if (MapEdit.players.contains(p.getName())) {
			MapEdit.players.remove(p.getName());
		}
	}
}
