package com.github;

import org.bukkit.entity.Player;

public enum CursorType {
    WHITE((byte) 0x0, null), 
    RED((byte) 0x2, "mapedit.color.red"), 
    GREEN((byte) 0x1, "mapedit.color.green"), 
    BLUE((byte) 0x3, "mapedit.color.blue"), 
    NON_EXISTANT((byte) 0xFFFFFFFF, "mapedit.hidden"),
    ENTITY((byte) 0x4, null);
    
    private byte colorByte;
    private String permission;

    CursorType(byte colorByte, String permission) {
        this.colorByte = colorByte;
    }

    public byte getByte() {
        return colorByte;
    }

    public String getPermission() {
        return permission;
    }

    public static CursorType setup(Player player) {
        CursorType color = CursorType.WHITE;
        for (CursorType testColor : values()) {
            String permission = testColor.getPermission();
            if (permission != null && player.hasPermission(permission)) {
                color = testColor;
            }
        }
        return color;
    }
}
