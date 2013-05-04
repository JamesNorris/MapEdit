package com.github;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

public class Events implements Listener {
    public static ArrayList<Short> registered = new ArrayList<Short>();
    private static Scale[] scales = new Scale[] {Scale.CLOSEST, Scale.CLOSE, Scale.NORMAL, Scale.FAR, Scale.FARTHEST};
    private static ConcurrentHashMap<String, Integer> playerMoveTimes = new ConcurrentHashMap<String, Integer>();

    /* Finds the players held item, and if it is a map, register a view for that durability */
    @EventHandler public void PIHE(final PlayerItemHeldEvent event) {
        startRendering(event.getPlayer().getItemInHand());
    }

    /* When the player joins, if they have a map in hand, registers its mapview
     * If the player has never been on before, gives them a map. */
    @EventHandler public void PJE(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        startRendering(p.getItemInHand());
        Rendering.renderEntities.put(p, CursorType.setup(p));
        /* Gives a player a map if they've never been on the server before */
        if (!p.hasPlayedBefore() && MapEdit.startingMap) {
            p.getInventory().addItem(new ItemStack(Material.MAP, 1));
        }
    }

    @EventHandler public void PME(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Rendering.renderers.containsValue(name)) {
            if (!playerMoveTimes.containsKey(name)) {
                playerMoveTimes.put(name, 0);
            }
            int current = playerMoveTimes.get(name);
            playerMoveTimes.remove(name);
            playerMoveTimes.put(name, current++);
            if (playerMoveTimes.get(name) >= 15) {
                for (Entity entity : player.getNearbyEntities(64, 64, 64)) {
                    if (!Rendering.renderEntities.containsKey(entity)) {
                        Rendering.renderEntities.put(entity, CursorType.ENTITY);
                    }
                }
            }
        }
    }

    private void startRendering(ItemStack mapItemInHand) {
        /* If the item in hand is a map, and the player has not yet been registered... */
        final short durability = mapItemInHand.getDurability();
        if (mapItemInHand.getType() == Material.MAP && !registered.contains(durability)) {
            final MapView m = Bukkit.getServer().getMap(durability);
            m.setScale(scales[MapEdit.defaultScale - 1]);
            /* If the mapview exists */
            if (m != null) {
                /* Clear its renderers, and add the custom renderer */
                m.getRenderers().clear();
                final MapRenderer mr = new Rendering();
                m.addRenderer(mr);
                registered.add(durability);
                /* Print out the debug string */
                if (MapEdit.DEBUG) {
                    MapEdit.log.info("[MapEdit] [Debug] Registered a MapView.");
                    MapEdit.registrationCount++;
                }
            }
        }
    }
}
