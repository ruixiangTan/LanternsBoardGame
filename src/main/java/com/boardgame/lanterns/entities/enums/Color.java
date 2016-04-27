package com.boardgame.lanterns.entities.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Ruixiang on 2/16/2016.
 */
public enum Color {

    ORANGE('O'), GREEN('G'), PURPLE('P'), WHITE('W'), BLUE('B'), RED('R'), BLACK('K');

    private static final List<Color> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final char key;

    Color(char key) {
        this.key = key;
    }

    public static Color randomColor() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
