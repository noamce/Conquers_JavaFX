package GameObjects;

import GameEngine.GameDescriptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class History implements Serializable
{
    private int turn;
    private List<Player> playerList;
    private Map <Integer,Territory>  MapList;

    public History(GameDescriptor descriptor,int turns)
    {
        this.MapList = new HashMap<>();
        descriptor.getTerritoryMap().forEach((integer, territory) -> this.MapList.put(integer,new Territory(territory)));
        this.playerList = new LinkedList<>();
        descriptor.getPlayersList().forEach(player -> this.playerList.add(new Player(player)));
        turn=turns;
    }

    public int getTurn() {
        return turn;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Map<Integer, Territory> getMapList() {
        return MapList;
    }

    public List<Player> getCopyOfPlayersList() {
        List<Player> copyOfPlayersList = new LinkedList<>();
        this.playerList.forEach(player -> copyOfPlayersList.add(new Player(player)));
        return copyOfPlayersList;
    }
    public Map<Integer,Territory> getCopyOfMap() {
        Map<Integer, Territory> copyOfUpdatedMap = new HashMap<>();
        this.MapList.forEach(((integer, territory) -> copyOfUpdatedMap.put(integer, new Territory(territory))));
        return copyOfUpdatedMap;
    }

    public void removePlayerFromHistorylist(Player player)
    {
        playerList.remove(player);
    }

    }

