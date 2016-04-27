package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.entities.enums.Direction;
import com.boardgame.lanterns.entities.enums.PlayerType;
import com.boardgame.lanterns.entities.strategies.GamePlayStrategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.boardgame.lanterns.services.DedicationForeCaster.getAvailableColors;

/**
 * Created by Ruixiang on 3/15/2016.
 */
public class AI extends Player {

    private GamePlayStrategy gamePlayStrategy;

    public AI(String name, GamePlayStrategy gamePlayStrategy) {
        super(name, PlayerType.AI);
        this.gamePlayStrategy = gamePlayStrategy;
    }

    @Override
    public void exchangeALanternCard(Game game, Color give, Color receive) {
        Color[] giveReceive = gamePlayStrategy.selectLanternCardsToExchange(game, this);
        if (giveReceive != null)
            super.exchangeALanternCard(game, giveReceive[0], giveReceive[1]);
    }

    @Override
    public void makeADedication(Game game, DedicationType placeHolder, List<Color> colors) {
        List<Color> colorLists = new ArrayList<>();
        DedicationType dedicationType = gamePlayStrategy.selectDedicationType(game, this);
        if (dedicationType == DedicationType.SEVEN_UNIQUE) {
            super.makeADedication(game, dedicationType, new ArrayList<Color>(Arrays.asList(Color.values())));
        }
        if (dedicationType == DedicationType.THREE_PAIRS) {
            for (Color color : getAvailableColors(this)) {
                Integer cards = getLanternCardStacks().get(color);
                if (cards >= 2)
                    colorLists.add(color);
                if (colorLists.size() == 3)
                    break;
            }
            super.makeADedication(game, dedicationType, colorLists);
        }

        if (dedicationType == DedicationType.FOUR_OF_A_KIND) {
            for (Color color : getAvailableColors(this)) {
                Integer cards = getLanternCardStacks().get(color);
                if (cards >= 4) {
                    colorLists.add(color);
                    break;
                }
            }
            super.makeADedication(game, dedicationType, colorLists);
        }
    }

    @Override
    public void placeALakeTile(Game game, LakeTile placeHolder1, Point placeHolder2) {
        Object[] actionResults = gamePlayStrategy.selectPlaceTileAction(game, this);
        LakeTile lt1 = (LakeTile) actionResults[0];
        getLakeTileList().remove(lt1);
        Direction direction = (Direction) actionResults[1];
        Point point = (Point) actionResults[2];
        lt1.rotate(direction.ordinal());
        super.placeALakeTile(game, lt1, point);
    }
}
