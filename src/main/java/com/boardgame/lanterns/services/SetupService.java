package com.boardgame.lanterns.services;

import com.boardgame.lanterns.entities.*;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.entities.enums.PlayerType;
import com.boardgame.lanterns.entities.strategies.GreedyStrategy;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Ruixiang on 3/13/2016.
 */
public class SetupService {

    private static final SetupService INSTANCE = new SetupService();
    private final Random random;

    private SetupService() {
        this.random = new Random();
    }

    public static SetupService getInstance() {
        return INSTANCE;
    }

    public void initiateGame(Game game, int numOfPlayers) {

        initiatePlayers(game, numOfPlayers);
        initiateLanternCardStack(game, numOfPlayers);
        initiateDedicationTokenStack(game, numOfPlayers);
        initiateLakeTileStack(game, numOfPlayers);
        initiateBoard(game);
        game.setFavorTokens(20);
        game.setCurrentPlayerIndex(0);

    }

    private void initiateBoard(Game game) {
        LakeTile[][] board = new LakeTile[8][8];
        board[3][3] = game.getLakeTileStack().remove(0);
        game.setBoard(board);
        game.setCurrentPlacedTile(board[3][3]);
        game.setCurrentPlacedTilePosition(new Point(3, 3));
    }

    private void initiateLakeTileStack(Game game, int numOfPlayers) {
        List<LakeTile> lakeTileStack = generateRandomLakeTiles(numOfPlayers);
        Collections.shuffle(lakeTileStack);
        game.setLakeTileStack(lakeTileStack);
    }

    private List<LakeTile> generateRandomLakeTiles(int numOfPlayers) {
        List<LakeTile> lakeTileList = new LinkedList<>();
        Color[] colors;
        int nums = 0;

        switch (numOfPlayers) {
            case 4:
                nums = (20 + 12 + 1);
                break;
            case 3:
                nums = (18 + 9 + 1);
                break;
            case 2:
                nums = (16 + 6 + 1);
        }

        for (int i = 0; i < nums; i++) {
            colors = new Color[4];
            for (int j = 0; j < colors.length; j++) {
                colors[j] = Color.randomColor();
            }
            lakeTileList.add(new LakeTile(colors, random.nextBoolean()));
        }
        return lakeTileList;
    }

    private void initiateDedicationTokenStack(Game game, int numOfPlayers) {
        Map<DedicationType, Deque<DedicationToken>> tokenStacksWrapper = new HashMap<>();
        Deque<DedicationToken> tokenStack;
        int points = 10;
        for (DedicationType dedicationType : DedicationType.values()) {
            tokenStack = new LinkedList<>();
            if (dedicationType == DedicationType.GENERIC)
                continue;

            for (int i = 9; i >= 1; i--) {
                switch (i) {
                    case 9:
                        tokenStack.add(new DedicationToken(dedicationType, points, 0));
                        break;
                    case 8:
                        tokenStack.add(new DedicationToken(dedicationType, points - 1, 4));
                        break;
                    case 7:
                        tokenStack.add(new DedicationToken(dedicationType, points - 1, 0));
                        break;
                    case 6:
                        tokenStack.add(new DedicationToken(dedicationType, points - 2, 3));
                        break;
                    case 5:
                        tokenStack.add(new DedicationToken(dedicationType, points - 2, 0));
                        break;
                    case 4:
                        tokenStack.add(new DedicationToken(dedicationType, points - 3, 3));
                        break;
                    case 3:
                        tokenStack.add(new DedicationToken(dedicationType, points - 3, 0));
                        break;
                    case 2:
                        if (dedicationType != DedicationType.FOUR_OF_A_KIND) {
                            tokenStack.add(new DedicationToken(dedicationType, points - 4, 0));
                            break;
                        } else {
                            tokenStack.add(new DedicationToken(dedicationType, points - 3, 0));
                            break;
                        }
                    case 1:
                        if (dedicationType != DedicationType.FOUR_OF_A_KIND) {
                            tokenStack.add(new DedicationToken(dedicationType, 5, 0));
                            break;
                        } else {
                            tokenStack.add(new DedicationToken(dedicationType, 4, 0));
                            break;
                        }
                }
            }
            points--;
            tokenStacksWrapper.put(dedicationType, tokenStack);
        }

        tokenStack = new LinkedList<>();
        tokenStack.add(new DedicationToken(DedicationType.GENERIC, 4, 0));
        tokenStack.add(new DedicationToken(DedicationType.GENERIC, 4, 0));
        tokenStack.add(new DedicationToken(DedicationType.GENERIC, 4, 0));
        tokenStacksWrapper.put(DedicationType.GENERIC, tokenStack);

        if (numOfPlayers != 4) {
            for (DedicationType dedicationType : DedicationType.values()) {
                Deque<DedicationToken> dedicationTokens = tokenStacksWrapper.get(dedicationType);
                Iterator<DedicationToken> iterator = dedicationTokens.iterator();
                while (iterator.hasNext()) {
                    int dots = iterator.next().getDots();
                    if (numOfPlayers == 3) {
                        if (dots == 4)
                            iterator.remove();
                    } else {
                        if (dots == 4 || dots == 3)
                            iterator.remove();
                    }
                }
            }
        }

        game.setDedicationTokenStacks(tokenStacksWrapper);
    }

    private void initiateLanternCardStack(Game game, int numOfPlayers) {

        int numOfLanternCards = 8;
        switch (numOfPlayers) {
            case 4:
                numOfLanternCards = 9;
                break;
            case 3:
                numOfLanternCards = 8;
                break;
            case 2:
                numOfLanternCards = 7;
                break;
        }
        Map<Color, Integer> lanternCardStacks = new HashMap<>();
        for (Color color : Color.values())
            lanternCardStacks.put(color, numOfLanternCards);
        game.setLanternCardStacks(lanternCardStacks);
    }

    private void initiatePlayers(Game game, int numOfPlayers) {

        String[] namesOfPlayer = {"You", "Jason", "Sarah", "Chris"};
        Player[] players = new Player[numOfPlayers];

        for (int i = 0; i < numOfPlayers; i++) {
            Player player;
            if (i == 0) {
                //player = new AI(namesOfPlayer[i], new GreedyStrategy());
                player = new Player(namesOfPlayer[i], PlayerType.HUMAN);
            } else {
                player = new AI(namesOfPlayer[i], new GreedyStrategy());
            }
            Map<Color, Integer> lanternCardWrapper = new HashMap<>();

            for (Color color : Color.values())
                lanternCardWrapper.put(color, 0);
            player.setLanternCardStacks(lanternCardWrapper);
            player.setDedications(new ArrayList<>());
            player.setLakeTileList(new LinkedList<>());
            player.setFavorTokens(0);
            players[i] = player;
        }

        game.setPlayers(players);
    }
}
