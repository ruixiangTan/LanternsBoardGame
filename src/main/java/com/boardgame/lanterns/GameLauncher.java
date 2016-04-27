package com.boardgame.lanterns;

import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.services.GamePlayService;
import com.boardgame.lanterns.services.SetupService;

/**
 * Created by Ruixiang on 3/13/2016.
 */
public class GameLauncher {

    public static void main(String[] args) {

        Game game = new Game();
        SetupService.getInstance().initiateGame(game, 2);
        GamePlayService.getInstance().play(game);

    }

}
