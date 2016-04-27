package com.boardgame.lanterns.services;

import com.boardgame.lanterns.entities.DedicationToken;
import com.boardgame.lanterns.entities.Game;
import com.boardgame.lanterns.entities.LakeTile;
import com.boardgame.lanterns.entities.Player;
import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.boardgame.lanterns.services.DedicationForeCaster.*;

/**
 * Created by Ruixiang on 3/14/2016.
 */
public class AuxiliaryServices {

    private static final AuxiliaryServices INSTANCE = new AuxiliaryServices();

    private AuxiliaryServices() {
    }

    public static AuxiliaryServices getInstance() {
        return INSTANCE;
    }

    public static List<DedicationType> getAvailableDedicationType(Player player) {
        List<DedicationType> dedicationTypeList = new ArrayList<>();
        if (isFourOfAKindPossible(player))
            dedicationTypeList.add(DedicationType.FOUR_OF_A_KIND);
        if (isThreePairsPossible(player))
            dedicationTypeList.add(DedicationType.THREE_PAIRS);
        if (isSevenUniquePossible(player))
            dedicationTypeList.add(DedicationType.SEVEN_UNIQUE);
        return dedicationTypeList;
    }

    public static boolean isExchangePossible(Player player) {
        if (player.getFavorTokens() < 2)
            return false;
        if (player.getLanternCardStacks().values().stream().mapToInt(i -> i.intValue()).sum() == 0)
            return false;
        return true;
    }

    public int getValidInput(final String message, final int min, final int max) {

        Scanner keyboard = new Scanner(System.in);
        System.out.println(message);
        int userChoice = 0;
        boolean valid = false;
        while (!valid) {
            try {
                userChoice = keyboard.nextInt();
                if (userChoice >= min && userChoice <= max) {
                    valid = true;
                }
            } catch (Exception e) {
                keyboard.nextLine();
            }
            if (!valid) {
                System.out.println("Invalid input. Please enter an integer between " + min + "-" + max);
            }
        }
        return userChoice;
    }

    public List<Point> getAvailablePositions(Game game) {

        LakeTile[][] board = game.getBoard();
        Set<Point> positionSet = new HashSet<>();
        Set<Point> visited = new HashSet<>();
        Deque<Point> queue = new ArrayDeque<>();
        Point current;

        queue.add(new Point(3, 3));
        visited.add(queue.peekFirst());

        while (!queue.isEmpty()) {
            current = queue.removeFirst();

            if (current.x + 1 < board.length) {
                if (board[current.x + 1][current.y] != null) {
                    if (!visited.contains(new Point(current.x + 1, current.y)))
                        queue.add(new Point(current.x + 1, current.y));
                } else {
                    positionSet.add(new Point(current.x + 1, current.y));
                }
            }

            if (current.x - 1 >= 0) {
                if (board[current.x - 1][current.y] != null) {
                    if (!visited.contains(new Point(current.x - 1, current.y)))
                        queue.add(new Point(current.x - 1, current.y));
                } else {
                    positionSet.add(new Point(current.x - 1, current.y));
                }
            }

            if (current.y + 1 < board.length) {
                if (board[current.x][current.y + 1] != null) {
                    if (!visited.contains(new Point(current.x, current.y + 1)))
                        queue.add(new Point(current.x, current.y + 1));
                } else {
                    positionSet.add(new Point(current.x, current.y + 1));
                }
            }

            if (current.y - 1 >= 0) {
                if (board[current.x][current.y - 1] != null) {
                    if (!visited.contains(new Point(current.x, current.y - 1)))
                        queue.add(new Point(current.x, current.y - 1));
                } else {
                    positionSet.add(new Point(current.x, current.y - 1));
                }
            }

            visited.add(current);
        }

        return new ArrayList<Point>(positionSet);
    }

    public double getValuePoints(Game game, LakeTile lakeTile, Point position) {
        Color[] colors = lakeTile.getTiles();
        LakeTile temp;
        LakeTile[][] board = game.getBoard();
        double points = 0;

        if (position.x + 1 < board.length && board[position.x + 1][position.y] != null) {
            temp = board[position.x + 1][position.y];
            if (colors[2].equals(temp.getTiles()[0])) {
                points++;
                if (temp.isPlatform())
                    points += 0.5;
            }
        }


        if (position.x - 1 >= 0 && board[position.x - 1][position.y] != null) {
            temp = board[position.x - 1][position.y];
            if (colors[0].equals(temp.getTiles()[2])) {
                points++;
                if (temp.isPlatform())
                    points += 0.5;
            }
        }

        if (position.y + 1 < board.length && board[position.x][position.y + 1] != null) {
            temp = board[position.x][position.y + 1];
            if (colors[1].equals(temp.getTiles()[3])) {
                points++;
                if (temp.isPlatform())
                    points += 0.5;
            }
        }

        if (position.y - 1 >= 0 && board[position.x][position.y - 1] != null) {
            temp = board[position.x][position.y - 1];
            if (colors[3].equals(temp.getTiles()[1])) {
                points++;
                if (temp.isPlatform())
                    points += 0.5;
            }
        }

        if (points > 0) {
            if (lakeTile.isPlatform())
                points += 0.5;
        }
        return points;
    }

    public int calculateScores(Player player) {
        int score = 0;
        List<DedicationToken> dedicationTokenList = player.getDedications();

        for (DedicationToken dedicationToken : dedicationTokenList) {
            score += dedicationToken.getPoints();
        }

        return score;
    }

    public void displayWinner(Game game) {

//        TODO Display Winner Method is missing
        System.out.println("Game is over, but display winner method currently haven't been implemented yet");

    }
}
