package GameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private int Id;
    private String playerName;
    private List<Integer> TerritoriesId;
    private int fund;
    private int color=0;



    public Player(int id, String playername, int funds,int color) {
        Id = id;
        this.playerName = playername;
        this.fund = funds;
        TerritoriesId = new ArrayList<>();
        this.color=color;
    }

    public Player(Player player) {
        Id = player.getID();
        this.playerName =player.getPlayer_name();
        this.fund = player.getFunds();
        this.TerritoriesId = new ArrayList<>();
        if (!player.getTerritoriesID().isEmpty())
            player.getTerritoriesID().forEach(territoryID -> this.TerritoriesId.add(new Integer(territoryID)));
    }
    public List<Integer> getTerritoriesID() {
        return TerritoriesId;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public String getPlayer_name() {
        return playerName;
    }
    public int getFunds() {
        return fund;
    }
    public int getID() {
        return Id;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }


        if (!(o instanceof Player)) {
            return false;
        }

        Player c = (Player) o;

        return this.Id == c.getID();
    }
    public void decrementFunds(int fundsAmount)
    {
        this.fund -= fundsAmount;
    }
    public void addTerritory(Territory territory)
    {
        TerritoriesId.add(territory.getID());
    }
    public void deleteTerritory(int territoryID)
    {
        int i=0;
        while (i<TerritoriesId.size())
        {
            if(TerritoriesId.get(i).intValue()==territoryID)
                TerritoriesId.remove(i);

            i++;

        }
    }
    public void incrementFunds(int fundsAmount)
    {
        this.fund += fundsAmount;
    }

}
