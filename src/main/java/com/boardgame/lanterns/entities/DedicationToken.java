package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.DedicationType;

/**
 * Created by Ruixiang on 2/17/2016.
 */
public class DedicationToken {

    private final DedicationType dedicationType;
    private final int points;
    private final int dots;

    public DedicationToken(DedicationType dedicationType, int points, int dots) {
        this.points = points;
        this.dedicationType = dedicationType;
        this.dots = dots;
    }

    public int getPoints() {
        return points;
    }

    public DedicationType getDedicationType() {
        return dedicationType;
    }

    public int getDots() {
        return dots;
    }
}
