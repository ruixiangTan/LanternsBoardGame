package com.boardgame.lanterns.entities.strategies;

import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.LakeTile;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.entities.enums.Direction;
import com.boardgame.lanterns.services.AuxiliaryServices;

import java.awt.*;
import java.util.List;
import java.util.TreeMap;

import static com.boardgame.lanterns.services.DedicationForeCaster.*;

/**
 * Created by Ruixiang on 3/17/2016.
 */
public class GreedyStrategy implements GamePlayStrategy {

    @Override
    public Color[] selectLanternCardsToExchange(Game game, Player player) {

        if (isSevenUniqueImminent(player)) {
            return exchangeToSevenUnique(game, player);
        }

        if (isThreePairsImminent(player)) {
            return exchangeToThreePairs(game, player);
        }

        if (isFourOfAKindImminent(player)) {
            return exchangeToFourOfAKind(game, player);
        }

        return null;

    }

    private Color[] exchangeToFourOfAKind(Game game, Player player) {
        Color[] giveReceive = new Color[2];
        List<Color> availableColors = getAvailableColors(player);
        List<Color> availableColorsInGame = getAvailableColorsInGame(game);

        for (Color color : availableColors) {
            if (player.getLanternCardStacks().get(color) < 3) {
                giveReceive[0] = color;
            }
            if (player.getLanternCardStacks().get(color) == 3) {
                if (availableColorsInGame.contains(color))
                    giveReceive[1] = color;
            }
        }

        if (giveReceive[0] == null)
        {
            for (Color color : availableColors) {
                if (player.getLanternCardStacks().get(color) == 3) {
                    giveReceive[0] = color;
                    break;
                }
            }
        }

        if (giveReceive[1] == null)
            return null;
        return giveReceive;
    }

    private Color[] exchangeToThreePairs(Game game, Player player) {
        Color[] giveReceive = new Color[2];
        List<Color> availableColors = getAvailableColors(player);
        List<Color> availableColorsInGame = getAvailableColorsInGame(game);

        for (Color color : availableColors) {
            if (player.getLanternCardStacks().get(color) > 2) {
                giveReceive[0] = color;
            }
            if (player.getLanternCardStacks().get(color) == 1) {
                if (availableColorsInGame.contains(color))
                    giveReceive[1] = color;
            }
        }

        if (giveReceive[0] == null) {
            for (Color color : availableColors) {
                if (player.getLanternCardStacks().get(color) == 1) {
                    giveReceive[0] = color;
                    break;
                }
            }
        }

        if (giveReceive[1] == null)
            return null;
        return giveReceive;
    }

    private Color[] exchangeToSevenUnique(Game game, Player player) {

        Color[] giveReceive = new Color[2];

        List<Color> availableColorsInGame = getAvailableColorsInGame(game);

        for (Color color : Color.values()) {
            if (player.getLanternCardStacks().get(color) > 1) {
                giveReceive[0] = color;
            }
            if (player.getLanternCardStacks().get(color) == 0) {
                if (availableColorsInGame.contains(color))
                    giveReceive[1] = color;
            }
        }

        if (giveReceive[1] == null)
            return null;
        return giveReceive;
    }

    @Override
    public DedicationType selectDedicationType(Game game, Player player) {
        if (isSevenUniquePossible(player))
            return DedicationType.SEVEN_UNIQUE;
        if (isThreePairsPossible(player))
            return DedicationType.THREE_PAIRS;
        if (isFourOfAKindPossible(player))
            return DedicationType.FOUR_OF_A_KIND;
        return null;
    }

    @Override
    public Object[] selectPlaceTileAction(Game game, Player player) {

        TreeMap<Double, Object[]> map = new TreeMap<>();

        for (LakeTile lt : player.getLakeTileList()) {
            for (Direction direction : Direction.values()) {
                lt.rotate(direction.ordinal());
                for (Point p : AuxiliaryServices.getInstance().getAvailablePositions(game)) {
                    double points = AuxiliaryServices.getInstance().getValuePoints(game, lt, p);
                    Object[] temp = new Object[]{lt, direction, p};
                    map.put(points, temp);
                }
                lt.rotate(4 - direction.ordinal());
            }
        }

        return map.lastEntry().getValue();

    }

}
