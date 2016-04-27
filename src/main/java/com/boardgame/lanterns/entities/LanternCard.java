package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.Color;

/**
 * Created by Ruixiang on 2/16/2016.
 */
public class LanternCard {

    private final Color color;

    public LanternCard(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
