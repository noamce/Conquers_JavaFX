package GameObjects;

import java.io.Serializable;

public class Territory implements Serializable {
    private int id;
    private int Threshold;
    private int profit;
    private Player conquer;
    private Army conquerArmyForce;

    public Territory(int ID,int profit, int Threshold) {
        this.id= ID;
        this.Threshold= Threshold;
        this.profit = profit;
        this.conquerArmyForce= null;
        this.conquer = null;
    }
    public Territory(Territory territory)
    {
        this.id= territory.getID();
        this.Threshold= territory.getArmyThreshold();
        this.profit = territory.getProfit();
        if(territory.getConquerArmyForce()!= null)
            this.conquerArmyForce= new Army(territory.getConquerArmyForce());
        if(territory.getConquer() != null)
            this.conquer = new Player(territory.getConquer());
    }

    public int getArmyThreshold() {
        return Threshold;
    }
    public int getProfit() {
        return profit;
    }
    public int getID() {
        return id;
    }
    public Army getConquerArmyForce() {return conquerArmyForce;}
    public Player getConquer() {
        return conquer;
    }
    public void setConquer(Player conquer) {
        this.conquer = conquer;
    }
    public void setConquerArmyForce(Army conquerArmyForce) {
        if(this.conquerArmyForce != null)
            this.conquerArmyForce.destroyArmy();
        this.conquerArmyForce= new Army(conquerArmyForce);
    }


    @Override
    public boolean equals(Object o) {


        if (o == this) {
            return true;
        }


        if (!(o instanceof Territory)) {
            return false;
        }


        Territory c = (Territory) o;

        return this.id == c.getID();
    }
    public Boolean isConquered() {
        return conquer != null;
    }
    public Boolean isArmyTotalPowerUnderThreshold() {
        return conquerArmyForce.getTotalPower() < this.getArmyThreshold();
    }
    public void eliminateThisWeakArmy() {
        conquerArmyForce.destroyArmy();
        conquerArmyForce=null;
        conquer.getTerritoriesID().remove(new Integer(this.getID()));
        conquer=null;
    }
    public void xChangeFundsForUnitsAndHold() {
        conquer.incrementFunds(conquerArmyForce.getArmyValueInFunds());
        eliminateThisWeakArmy();
    }
    public void reduceCompetence() {
        conquerArmyForce.reduceCompetence();
    }
    public void rehabilitateConquerArmy() { conquerArmyForce.rehabilitateArmy();}
    public int getRehabilitationArmyPriceInTerritory()
    {
        return this.conquerArmyForce.calculateRehabilitationPrice();
    }


    public void RemoveFromTerritory(int id)
    {
        if(this.getConquer()!=null && this.getConquer().getID() == id )
        {
          eliminateThisWeakArmy();
        }
    }
}
