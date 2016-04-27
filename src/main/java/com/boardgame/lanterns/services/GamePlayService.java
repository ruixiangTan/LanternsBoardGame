package com.boardgame.lanterns.services;

import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.LakeTile;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.entities.enums.Direction;
import com.boardgame.lanterns.entities.enums.PlayerType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.boardgame.lanterns.services.AuxiliaryServices.getAvailableDedicationType;
import static com.boardgame.lanterns.services.AuxiliaryServices.isExchangePossible;
import static com.boardgame.lanterns.services.DedicationForeCaster.*;
import static com.boardgame.lanterns.utilities.CommandLinePrinter.*;

/**
 * Created by Ruixiang on 3/20/2016.
 */
public class GamePlayService {

    private static GamePlayService ourInstance = new GamePlayService();
    private final AuxiliaryServices auxiliaryServices;

    private GamePlayService() {
        auxiliaryServices = AuxiliaryServices.getInstance();
    }

    public static GamePlayService getInstance() {
        return ourInstance;
    }

    public void play(Game game) {
        dealLakeTiles(game, 3);
        dealLanternCards(game);
        printCurrentGameStatus(game);

        while (!game.isEnded()) {
            Player player = game.getCurrentPlayer();
            System.out.println("Round: " + player.getName());
            if (player.getPlayerType() == PlayerType.HUMAN) {
                if (isExchangePossible(player)) {
                    performExchange(game, player);
                    System.out.println("After Exchange: ");
                    printStatusInfo(game);
                }
                if (isDedicationPossible(player)) {
                    performDedication(game, player);
                    System.out.println("After Dedication: ");
                    printStatusInfo(game);
                }
                performPlaceTile(game, player);
            } else {
                if (isExchangePossible(player)) {
                    player.exchangeALanternCard(game, null, null);
                    System.out.println("After Exchange: ");
                    printStatusInfo(game);
                }
                if (isDedicationPossible(player)) {
                    player.makeADedication(game, null, null);
                    System.out.println("After Dedication: ");
                    printStatusInfo(game);
                }
                player.placeALakeTile(game, null, null);
            }

            dealLakeTiles(game, 1);
            dealLanternCards(game);
            dealBonus(game);
            printCurrentGameStatus(game);
            game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().length);
        }

        finalRound(game);
        auxiliaryServices.displayWinner(game);
    }

    private void finalRound(Game game) {

        for (Player player : game.getPlayers()) {
            System.out.println("Final Round: " + player.getName());
            if (player.getPlayerType() == PlayerType.HUMAN) {
                if (isExchangePossible(player)) {
                    performExchange(game, player);
                    printStatusInfo(game);
                }
                if (isDedicationPossible(player)) {
                    performDedication(game, player);
                    printStatusInfo(game);
                }
            } else {
                if (isExchangePossible(player)) {
                    player.exchangeALanternCard(game, null, null);
                    printStatusInfo(game);
                }
                if (isDedicationPossible(player)) {
                    player.makeADedication(game, null, null);
                    printStatusInfo(game);
                }
            }
        }
    }

    private void performPlaceTile(Game game, Player player) {
        printAvailableTiles(player);
        int lakeTileIndex = auxiliaryServices.getValidInput("Which lake tile to place?", 0, player.getLakeTileList().size() - 1);
        LakeTile lakeTile = player.getLakeTileList().remove(lakeTileIndex);
        System.out.println(Arrays.toString(Direction.values()));
        int directionIndex = auxiliaryServices.getValidInput("Which direction to place the tile?", 0, Direction.values().length - 1);
        lakeTile.rotate(directionIndex);
        List<Point> positions = auxiliaryServices.getAvailablePositions(game);
        printAvailablePositions(positions);
        int positionIndex = auxiliaryServices.getValidInput("Which position would you like to place the tile?", 0, positions.size() - 1);
        player.placeALakeTile(game, lakeTile, positions.get(positionIndex));
    }

    private void performDedication(Game game, Player player) {
        int userChoice = auxiliaryServices.getValidInput("Make a dedication? Enter 1: yes 0: no", 0, 1);
        if (userChoice == 1) {
            List<Color> selected = new ArrayList<>();
            List<Color> availableColors = getAvailableColors(player);
            List<DedicationType> availableDedicationType = getAvailableDedicationType(player);
            System.out.println(availableDedicationType);
            int type = auxiliaryServices.getValidInput("Which dedication type do you want to make? ", 0, availableDedicationType.size());
            {
                switch (availableDedicationType.get(type)) {
                    case FOUR_OF_A_KIND:
                        printAvailableColors(availableColors);
                        userChoice = auxiliaryServices.getValidInput("Please select a color to return?", 0, availableColors.size());
                        selected.add(availableColors.get(userChoice));
                        player.makeADedication(game, DedicationType.FOUR_OF_A_KIND, selected);
                        break;
                    case THREE_PAIRS:
                        printAvailableColors(availableColors);
                        userChoice = auxiliaryServices.getValidInput("Please select a color 1 to return?", 0, availableColors.size());
                        selected.add(availableColors.get(userChoice));
                        printAvailableColors(availableColors);
                        userChoice = auxiliaryServices.getValidInput("Please select a color 2 to return?", 0, availableColors.size());
                        selected.add(availableColors.get(userChoice));
                        printAvailableColors(availableColors);
                        userChoice = auxiliaryServices.getValidInput("Please select a color 3 to return?", 0, availableColors.size());
                        selected.add(availableColors.get(userChoice));
                        player.makeADedication(game, DedicationType.THREE_PAIRS, selected);
                        break;
                    case SEVEN_UNIQUE:
                        player.makeADedication(game, DedicationType.SEVEN_UNIQUE, availableColors);
                        break;
                }
            }
        }
    }

    private void performExchange(Game game, Player player) {
        int userChoice = auxiliaryServices.getValidInput("Exchange Favor Tokens? Enter 1: yes 0: no", 0, 1);
        if (userChoice == 1) {
            List<Color> availableColors = getAvailableColors(player);
            printAvailableColors(availableColors);
            int from = auxiliaryServices.getValidInput("Which color to give out?", 0, availableColors.size());
            List<Color> availableColorsInGame = getAvailableColorsInGame(game);
            printAvailableColors(availableColorsInGame);
            int to = auxiliaryServices.getValidInput("Which color to receive?", 0, availableColorsInGame.size());
            player.exchangeALanternCard(game, availableColors.get(from), availableColorsInGame.get(to));
        }
    }

    private void dealLakeTiles(Game game, int nums) {
        Player[] players = game.getPlayers();
        List<LakeTile> tiles = game.getLakeTileStack();
        for (Player player : players) {
            for (int i = 0; i < nums; i++) {
                if (game.getLakeTileStack().size() > 0 && player.getLakeTileList().size() < 3)
                    player.getLakeTileList().add(tiles.remove(0));
            }
        }
    }

    private void dealLanternCards(Game game) {
        Color[] colors = game.getCurrentPlacedTile().getTiles();
        Player[] players = game.getPlayers();

        for (int i = 0; i < players.length; i++) {
            Integer cards;
            if (game.getLanternCardStacks().get(colors[i]) > 0) {
                cards = players[i].getLanternCardStacks().get(colors[i]);
                players[i].getLanternCardStacks().put(colors[i], ++cards);
                cards = game.getLanternCardStacks().get(colors[i]);
                game.getLanternCardStacks().put(colors[i], --cards);
            }
        }
    }

    private void dealBonus(Game game) {
        Player player = game.getCurrentPlayer();
        Point position = game.getCurrentPlacedTilePosition();
        Color[] colors = game.getCurrentPlacedTile().getTiles();
        LakeTile temp;
        LakeTile[][] board = game.getBoard();
        double points = 0;

        if (position.x + 1 < board.length && board[position.x + 1][position.y] != null) {
            temp = board[position.x + 1][position.y];
            if (colors[2].equals(temp.getTiles()[0])) {
                points++;
                if (game.getLanternCardStacks().get(colors[2]) > 0) {
                    Integer cards = player.getLanternCardStacks().get(colors[2]);
                    player.getLanternCardStacks().put(colors[2], ++cards);
                    cards = game.getLanternCardStacks().get(colors[2]);
                    game.getLanternCardStacks().put(colors[2], --cards);
                }
                if (temp.isPlatform())
                    dealFavorTokens(game, player);
            }
        }

        if (position.x - 1 >= 0 && board[position.x - 1][position.y] != null) {
            temp = board[position.x - 1][position.y];
            if (colors[0].equals(temp.getTiles()[2])) {
                points++;
                if (game.getLanternCardStacks().get(colors[0]) > 0) {
                    Integer cards = player.getLanternCardStacks().get(colors[0]);
                    player.getLanternCardStacks().put(colors[0], ++cards);
                    cards = game.getLanternCardStacks().get(colors[0]);
                    game.getLanternCardStacks().put(colors[0], --cards);
                }
                if (temp.isPlatform())
                    dealFavorTokens(game, player);
            }
        }

        if (position.y + 1 < board.length && board[position.x][position.y + 1] != null) {
            temp = board[position.x][position.y + 1];
            if (colors[1].equals(temp.getTiles()[3])) {
                points++;
                if (game.getLanternCardStacks().get(colors[1]) > 0) {
                    Integer cards = player.getLanternCardStacks().get(colors[1]);
                    player.getLanternCardStacks().put(colors[1], ++cards);
                    cards = game.getLanternCardStacks().get(colors[1]);
                    game.getLanternCardStacks().put(colors[1], --cards);
                }
                if (temp.isPlatform())
                    dealFavorTokens(game, player);
            }
        }

        if (position.y - 1 >= 0 && board[position.x][position.y - 1] != null) {
            temp = board[position.x][position.y - 1];
            if (colors[3].equals(temp.getTiles()[1])) {
                points++;
                if (game.getLanternCardStacks().get(colors[3]) > 0) {
                    Integer cards = player.getLanternCardStacks().get(colors[3]);
                    player.getLanternCardStacks().put(colors[3], ++cards);
                    cards = game.getLanternCardStacks().get(colors[3]);
                    game.getLanternCardStacks().put(colors[3], --cards);
                }
                if (temp.isPlatform())
                    dealFavorTokens(game, player);
            }
        }

        if (points > 0) {
            if (game.getCurrentPlacedTile().isPlatform())
                dealFavorTokens(game, player);
        }
    }

    private void dealFavorTokens(Game game, Player currentPlayer) {
        if (game.getFavorTokens() > 0) {
            game.setFavorTokens(game.getFavorTokens() - 1);
            currentPlayer.setFavorTokens(currentPlayer.getFavorTokens() + 1);
        }
    }
}
