package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;

import java.awt.*;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruixiang on 2/17/2016.
 */
public class Game {

    private int favorTokens;
    private Player[] players;
    private LakeTile[][] board;
    private Map<Color, Integer> lanternCardStacks;
    private Map<DedicationType, Deque<DedicationToken>> dedicationTokenStacks;
    private List<LakeTile> lakeTileStack;
    private LakeTile currentPlacedTile;
    private Point currentPlacedTilePosition;
    private int currentPlayerIndex;

    public int getFavorTokens() {
        return favorTokens;
    }

    public void setFavorTokens(int favorTokens) {
        this.favorTokens = favorTokens;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public LakeTile[][] getBoard() {
        return board;
    }

    public void setBoard(LakeTile[][] board) {
        this.board = board;
    }

    public Map<Color, Integer> getLanternCardStacks() {
        return lanternCardStacks;
    }

    public void setLanternCardStacks(Map<Color, Integer> lanternCardStacks) {
        this.lanternCardStacks = lanternCardStacks;
    }

    public Map<DedicationType, Deque<DedicationToken>> getDedicationTokenStacks() {
        return dedicationTokenStacks;
    }

    public void setDedicationTokenStacks(Map<DedicationType, Deque<DedicationToken>> dedicationTokenStacks) {
        this.dedicationTokenStacks = dedicationTokenStacks;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public boolean isEnded() {
        int empty = 0;
        for (Player player : players) {
            if (player.getLakeTileList().size() == 0)
                empty++;
        }
        return (lakeTileStack.size() == 0) && (empty == players.length);
    }

    public void setEnded(boolean ended) {
    }

    public List<LakeTile> getLakeTileStack() {
        return lakeTileStack;
    }

    public void setLakeTileStack(List<LakeTile> lakeTileStack) {
        this.lakeTileStack = lakeTileStack;
    }

    public LakeTile getCurrentPlacedTile() {
        return currentPlacedTile;
    }

    public void setCurrentPlacedTile(LakeTile currentPlacedTile) {
        this.currentPlacedTile = currentPlacedTile;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Point getCurrentPlacedTilePosition() {
        return currentPlacedTilePosition;
    }

    public void setCurrentPlacedTilePosition(Point currentPlacedTilePosition) {
        this.currentPlacedTilePosition = currentPlacedTilePosition;
    }
}
