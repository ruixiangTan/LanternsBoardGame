package com.boardgame.lanterns.entities;

import com.boardgame.lanterns.entities.enums.Color;
import com.boardgame.lanterns.entities.enums.DedicationType;
import com.boardgame.lanterns.entities.enums.PlayerType;

import java.awt.*;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruixiang on 2/17/2016.
 */
public class Player {

    private final String name;
    private final PlayerType playerType;
    private Map<Color, Integer> lanternCardStacks;
    private List<DedicationToken> dedications;
    private List<LakeTile> lakeTileList;
    private int favorTokens;

    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
    }

    public String getName() {
        return name;
    }

    public Map<Color, Integer> getLanternCardStacks() {
        return lanternCardStacks;
    }

    public void setLanternCardStacks(Map<Color, Integer> lanternCardStacks) {
        this.lanternCardStacks = lanternCardStacks;
    }

    public List<DedicationToken> getDedications() {
        return dedications;
    }

    public void setDedications(List<DedicationToken> dedications) {
        this.dedications = dedications;
    }

    public List<LakeTile> getLakeTileList() {
        return lakeTileList;
    }

    public void setLakeTileList(List<LakeTile> lakeTileList) {
        this.lakeTileList = lakeTileList;
    }

    public int getFavorTokens() {
        return favorTokens;
    }

    public void setFavorTokens(int favorTokens) {
        this.favorTokens = favorTokens;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void exchangeALanternCard(Game game, Color give, Color receive) {
        favorTokens -= 2;
        game.setFavorTokens(game.getFavorTokens() + 2);

        Integer oldValue = lanternCardStacks.get(give);
        lanternCardStacks.put(give, --oldValue);

        oldValue = lanternCardStacks.get(receive);
        lanternCardStacks.put(receive, ++oldValue);

        oldValue = game.getLanternCardStacks().get(receive);
        game.getLanternCardStacks().put(receive, --oldValue);

        oldValue = game.getLanternCardStacks().get(give);
        game.getLanternCardStacks().put(give, ++oldValue);
    }

    public void makeADedication(Game game, DedicationType dedicationType, List<Color> colors) {

        int cardsToBeRemoved = 0;
        Integer oldValue;

        switch (dedicationType) {
            case FOUR_OF_A_KIND:
                cardsToBeRemoved = 4;
                break;
            case THREE_PAIRS:
                cardsToBeRemoved = 2;
                break;
            case SEVEN_UNIQUE:
                cardsToBeRemoved = 1;
                break;
        }

        for (Color fromTo : colors) {
            oldValue = lanternCardStacks.get(fromTo);
            lanternCardStacks.put(fromTo, oldValue - cardsToBeRemoved);

            oldValue = game.getLanternCardStacks().get(fromTo);
            game.getLanternCardStacks().put(fromTo, oldValue + cardsToBeRemoved);
        }

        Deque<DedicationToken> dedicationTokenStack = game.getDedicationTokenStacks().get(dedicationType);
        Deque<DedicationToken> genericDedicationTokenStack = game.getDedicationTokenStacks().get(DedicationType.GENERIC);
        if (dedicationTokenStack.size() != 0) {
            dedications.add(dedicationTokenStack.removeFirst());
        } else {
            if (genericDedicationTokenStack.size() == 0)
                dedications.add(new DedicationToken(DedicationType.GENERIC, 4, 0));
            else
                dedications.add((genericDedicationTokenStack.removeFirst()));
        }

    }

    public void placeALakeTile(Game game, LakeTile lakeTile, Point position) {
        LakeTile[][] board = game.getBoard();
        board[position.x][position.y] = lakeTile;
        game.setCurrentPlacedTile(lakeTile);
        game.setCurrentPlacedTilePosition(position);
    }

}
