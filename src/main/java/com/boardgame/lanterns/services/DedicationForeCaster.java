package com.boardgame.lanterns.services;

import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruixiang on 3/19/2016.
 */
public final class DedicationForeCaster {

    private DedicationForeCaster() {
    }

    public static boolean isDedicationPossible(Player player) {

        return isFourOfAKindPossible(player) || isThreePairsPossible(player) || isSevenUniquePossible(player);

    }

    public static List<Color> getAvailableColors(Player player) {
        List<Color> availableColors = new ArrayList<>();
        Map<Color, Integer> lanternCardWrapper = player.getLanternCardStacks();
        for (Color color : Color.values()) {
            if (lanternCardWrapper.get(color) > 0)
                availableColors.add(color);
        }
        return availableColors;
    }


    public static List<Color> getAvailableColorsInGame(Game game) {
        List<Color> availableColors = new ArrayList<>();
        Map<Color, Integer> lanternCardWrapper = game.getLanternCardStacks();
        for (Color color : Color.values()) {
            if (lanternCardWrapper.get(color) > 0)
                availableColors.add(color);
        }
        return availableColors;
    }

    public static boolean isSevenUniquePossible(Player player) {
        return getAvailableColors(player).size() == 7 ? true : false;
    }

    public static boolean isThreePairsPossible(Player player) {

        List<Color> availableColors = getAvailableColors(player);
        if (availableColors.size() < 3)
            return false;
        int threePairsCounter = 0;
        for (Color color : availableColors) {
            Integer cards = player.getLanternCardStacks().get(color);
            if (cards >= 2)
                threePairsCounter++;
        }

        return threePairsCounter >= 3;

    }

    public static boolean isFourOfAKindPossible(Player player) {

        for (Color color : getAvailableColors(player)) {
            Integer cards = player.getLanternCardStacks().get(color);
            if (cards >= 4)
                return true;
        }
        return false;
    }

    public static boolean isThreePairsImminent(Player player) {
        List<Color> availableColors = getAvailableColors(player);
        if (availableColors.size() < 3)
            return false;
        int sum = 0;
        for (Color color : availableColors)
            sum += player.getLanternCardStacks().get(color);
        if (sum >= 6)
            return true;
        return false;
    }

    public static boolean isFourOfAKindImminent(Player player) {
        List<Color> availableColors = getAvailableColors(player);
        if (availableColors.size() < 2)
            return false;
        for (Color color : availableColors)
            if (player.getLanternCardStacks().get(color) == 3)
                return true;
        return false;
    }

    public static boolean isSevenUniqueImminent(Player player) {

        List<Color> availableColors = getAvailableColors(player);
        if (availableColors.size() < 6)
            return false;
        for (Color color : availableColors)
            if (player.getLanternCardStacks().get(color) > 1)
                return true;
        return false;
    }
}
