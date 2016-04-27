package com.boardgame.lanterns.utilities;

import com.boardgame.lanterns.entities.DedicationToken;
import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.LakeTile;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.services.AuxiliaryServices;

import java.awt.*;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruixiang on 3/24/2016.
 */
public final class CommandLinePrinter {

    private CommandLinePrinter() {
    }

    public static void printAvailablePositions(java.util.List<Point> positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " : " + "[x=" + positions.get(i).x + ",y=" + positions.get(i).y + "]");
        }
    }

    public static void printCurrentGameStatus(Game game) {
        printBoard(game);
        printStatusInfo(game);
    }

    public static void printStatusInfo(Game game) {

        Map<Color, Integer> lanternCardStacks = game.getLanternCardStacks();
        Map<DedicationType, Deque<DedicationToken>> tokenStacks = game.getDedicationTokenStacks();
        Player[] players = game.getPlayers();
        String result = String.format("Game: L-%1$d, F-%2$d, FT-%3$d, TT-%4$d, ST-%5$d, GT-%6$d, 'O'-%7$s, 'G'-%8$s, 'P'-%9$s, 'W'-%10$s, 'B'-%11$s, 'R'-%12$s, 'K'-%13$s",
                game.getLakeTileStack().size(), game.getFavorTokens(), tokenStacks.get(DedicationType.FOUR_OF_A_KIND).size(), tokenStacks.get(DedicationType.THREE_PAIRS).size(), tokenStacks.get(DedicationType.SEVEN_UNIQUE).size(), tokenStacks.get(DedicationType.GENERIC).size(), lanternCardStacks.get(Color.ORANGE), lanternCardStacks.get(Color.GREEN), lanternCardStacks.get(Color.PURPLE), lanternCardStacks.get(Color.WHITE), lanternCardStacks.get(Color.BLUE), lanternCardStacks.get(Color.RED), lanternCardStacks.get(Color.BLACK));
        System.out.println(result);

        for (Player player : players) {
            lanternCardStacks = player.getLanternCardStacks();
            result = String.format("%1$s: L-%2$d, F-%3$d, Scores-%4$d, 'O'-%5$s, 'G'-%6$s, 'P'-%7$s, 'W'-%8$s, 'B'-%9$s, 'R'-%10$s, 'K'-%11$s",
                    player.getName(), player.getLakeTileList().size(), player.getFavorTokens(), AuxiliaryServices.getInstance().calculateScores(player), lanternCardStacks.get(Color.ORANGE), lanternCardStacks.get(Color.GREEN), lanternCardStacks.get(Color.PURPLE), lanternCardStacks.get(Color.WHITE), lanternCardStacks.get(Color.BLUE), lanternCardStacks.get(Color.RED), lanternCardStacks.get(Color.BLACK));
            System.out.println(result);
        }

        System.out.println();
    }

    private static void printBoard(Game game) {
        LakeTile[][] board = game.getBoard();
        StringBuffer[] lines1 = new StringBuffer[board.length];
        StringBuffer[] lines2 = new StringBuffer[board.length];
        StringBuffer[] lines3 = new StringBuffer[board.length];

        for (int i = 0; i < board.length; i++) {
            lines1[i] = new StringBuffer();
            lines2[i] = new StringBuffer();
            lines3[i] = new StringBuffer();

            for (int j = 0; j < board[i].length; j++) {
                if (j == 0) {
                    lines1[i].append("\t");
                    lines2[i].append(i + "\t");
                    lines3[i].append("\t");
                }

                String[] tileLines = LakeTile.getTextString(board[i][j]);
                lines1[i].append(tileLines[0]);
                lines2[i].append(tileLines[1]);
                lines3[i].append(tileLines[2]);
            }
        }

        if (board.length > 0) {
            System.out.print("\t");
            for (int i = 0; i < board[0].length; i++) {
                System.out.printf("%4d   ", i);
            }
            System.out.println();
        }
        for (int i = 0; i < board.length; i++) {
            System.out.println(lines1[i]);
            System.out.println(lines2[i]);
            System.out.println(lines3[i]);
            System.out.println();
        }
    }

    public static void printAvailableColors(List<Color> availableColors) {
        for (int i = 0; i < availableColors.size(); i++) {
            System.out.print(i + ":" + availableColors.get(i));
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void printAvailableTiles(Player player) {
        for (int i = 0; i < 3; i++) {
            for (LakeTile lakeTile : player.getLakeTileList()) {
                String[] temp = LakeTile.getTextString(lakeTile);
                System.out.print(temp[i] + "\t");
            }
            System.out.println();
        }
    }
}
