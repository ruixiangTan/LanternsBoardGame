package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.Color;

/**
 * Created by Ruixiang on 2/17/2016.
 */
public class LakeTile {

    private final Color[] tiles;
    private final boolean isPlatform;

    public LakeTile(Color[] tiles, boolean isPlatform) {
        this.tiles = tiles;
        this.isPlatform = isPlatform;
    }

    public static String[] getTextString(LakeTile lt) {
        String[] lines = new String[3];
        if (lt == null) {
            lines[0] = "[     ]";
            lines[1] = "[     ]";
            lines[2] = "[     ]";
        } else {
            Color[] tiles = lt.getTiles();
            char plat = lt.isPlatform() ? '1' : '0';
            lines[0] = "[  " + tiles[0].key + "  ]";
            lines[1] = "[" + tiles[3].key + " " + plat + " " + tiles[1].key + "]";
            lines[2] = "[  " + tiles[2].key + "  ]";
        }
        return lines;
    }

    public void rotate(int k) {

        Color[] result = new Color[tiles.length];

        for (int i = 0; i < k; i++) {
            result[i] = tiles[tiles.length - k + i];
        }

        int j = 0;
        for (int i = k; i < tiles.length; i++) {
            result[i] = tiles[j];
            j++;
        }
        System.arraycopy(result, 0, tiles, 0, tiles.length);
    }

    public boolean isPlatform() {
        return isPlatform;
    }

    public Color[] getTiles() {
        return tiles;
    }

}
