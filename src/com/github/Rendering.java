package com.github;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

public class Rendering extends MapRenderer {
    public static ConcurrentHashMap<Rendering, String> renderers = new ConcurrentHashMap<Rendering, String>();
    public static ConcurrentHashMap<Entity, CursorType> renderEntities = new ConcurrentHashMap<Entity, CursorType>();
    public static Scale scale = Scale.NORMAL;
    private CopyOnWriteArrayList<MapCursor> mapCursors = new CopyOnWriteArrayList<MapCursor>();
    private int renderDelay = MapEdit.renderDelay;
    private boolean clearedCanvas;

    /* A custom map renderer, that changes the default map renderer (render() is called once a second) */
    @Override public void render(final MapView map, final MapCanvas canvas, final Player player) {
        if (!renderers.containsKey(this)) {
            renderers.put(this, player.getName());
        }
        /* Wait for the player ping */
        --renderDelay;
        if (renderDelay <= 0) {
            renderDelay = MapEdit.renderDelay;
            /* Renders player cursors on the map */
            final MapCursorCollection canvasCursors = canvas.getCursors();
            canvas.setCursors(canvasCursors);
            /* Removes cursors left over from the plugin restart */
            if (clearedCanvas == false) {
                for (int n = 0; n < canvasCursors.size(); n++) {
                    MapCursor mc = canvasCursors.getCursor(n);
                    canvasCursors.removeCursor(mc);
                }
                clearedCanvas = true;
            }
            /* REMOVAL OF OLD CHANGES */
            /* Removes placed cursors from the last render cycle */
            if (!mapCursors.isEmpty()) {
                for (MapCursor mc : mapCursors) {
                    canvasCursors.removeCursor(mc);
                    mapCursors.remove(mc);
                }
            }
            /* Find all players in the game */
            for (Entity renderEntity : renderEntities.keySet()) {
                if (renderEntity.isDead() && !(renderEntity instanceof Player)) {
                    renderEntities.remove(renderEntity);
                    return;
                }
                byte cursorcolor = renderEntities.get(renderEntity).getByte();
                /* PERMISSIONS AND COLORS */
                if (cursorcolor != 0xFFFFFFFF && (renderEntity != player && MapEdit.socialMode)) {
                    /* Changes the cursor color based on player permissions and config */
                    short mapId = -1;
                    if (renderEntity instanceof Player) {
                        /* Make sure the 2 players don't share a mapview, before placing a cursor */
                        Player other = (Player) renderEntity;
                        ItemStack it = other.getItemInHand();
                        if (it.getType() == Material.MAP) {
                            mapId = it.getDurability();
                        }
                    }
                    if (Bukkit.getServer().getMap(mapId) != map) {
                        /* ADDITION OF CURSORS */
                        /* Get the center of the map */
                        int centerX = map.getCenterX();
                        int centerZ = map.getCenterZ();
                        /* Get the scale of the map, and keep it between 0 and 4 */
                        int scale = map.getScale().getValue();
                        if (scale < 0) {
                            scale = 0;
                        }
                        if (scale > 4) {
                            scale = 4;
                        }
                        /* Find the edges of the map
                         * 
                         * Now start the equations you will not understand... */
                        float f = (float) (renderEntity.getLocation().getX() - centerX) / (1 << scale);
                        float f1 = (float) (renderEntity.getLocation().getZ() - centerZ) / (1 << scale);
                        byte b0 = 64;
                        byte b1 = 64;
                        /* If the cursor is on the map... */
                        if (f >= (-b0) && f1 >= (-b1) && f <= b0 && f1 <= b1) {
                            byte b2 = cursorcolor;
                            /* Get the x and y of the player on the map */
                            byte b3 = (byte) ((int) ((f * 2.0F) + 0.5D));
                            byte b4 = (byte) ((int) ((f1 * 2.0F) + 0.5D));
                            /* Get the player yaw, and convert it to an int 0 - 15
                             * 
                             * UNKNOWN BUG
                             * When rotating at a certain angle, a blue cursor turns green,
                             * however, a print says it is still blue.
                             * This must be a Bukkit/Minecraft bug */
                            double dir = renderEntity.getLocation().getYaw() * 16.0D / 360.0D;
                            /* If b5 is less than 0, change it to the positive of the same location
                             * NORTH = 8 */
                            byte direction = (byte) ((int) dir & 0xF);
                            /* Add the cursor */
                            MapCursor mc = new MapCursor(b3, b4, (byte) direction, b2, true);
                            canvasCursors.addCursor(mc);
                            mapCursors.add(mc);
                        }
                    }
                }
            }
            ++MapEdit.renderCount;
        }
    }
}
