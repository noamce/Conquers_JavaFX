package GameObjects;

import java.io.Serializable;

public class Unit implements Serializable {
    private String type;
    private int rank;

    private int competenceReduction;
    private int currentFP;
    private int purchase;
    private int maxFP;

    public Unit(String type, int rank, int purchase, int maxFP, int competenceReduction) {
        this.type = type;
        this.rank = rank;
        this.purchase = purchase;
        this.maxFP = maxFP;
        this.competenceReduction = competenceReduction;
        this.currentFP = maxFP;
    }
    public Unit(Unit unit)
    {
        this.type = unit.getType();
        this.rank = unit.getRank();
        this.purchase = unit.getPurchase();
        this.maxFP = unit.getMaxFirePower();
        this.competenceReduction = unit.getCompetenceReduction();
        this.currentFP = unit.getCurrentFirePower();
    }

    public int getRank() {return rank;}
    public String getType() {
        return type;
    }
    public void setType(String type)
    {
        this.type=type;
    }
    public int getMaxFirePower() {
        return maxFP;
    }
    public int getCurrentFirePower() {
        return currentFP;
    }
    public int getCompetenceReduction() {
        return competenceReduction;
    }
    public int getPurchase() {
        return purchase;
    }



    public double calculateRehabilitationPrice() {
        return ((double)this.purchase / (double)this.maxFP)*(this.getMaxFirePower() - this.getCurrentFirePower());
    }
    public void reduceCompetenceByPercent(double lossPercentage) {
        if(this.currentFP >= competenceReduction)
            this.currentFP = (int)((currentFP* lossPercentage)+0.5);
        else
            this.type = "Dead";
    }
    public void rehabilitateUnit() {currentFP = maxFP;}
    public void reduceCompetence() {
        if(this.currentFP >= competenceReduction)
            this.currentFP -= competenceReduction;
        else
            this.type = "Dead";
    }

}
