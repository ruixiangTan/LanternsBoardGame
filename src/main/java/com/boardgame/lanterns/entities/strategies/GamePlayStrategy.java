package com.boardgame.lanterns.entities.strategies;

import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;

/**
 * Created by Ruixiang on 3/17/2016.
 */
public interface GamePlayStrategy {

    Color[] selectLanternCardsToExchange(Game game, Player player);

    DedicationType selectDedicationType(Game game, Player player);

    Object[] selectPlaceTileAction(Game game, Player player);
}
