package GameObjects;
import GameEngine.GameManager;

import java.io.Serializable;
import java.util.*;

public class Army implements Serializable {
    private List<Unit> Units;
    private int potentialPower;
    private int totalPower;

    public Army(Army army) {
        this.Units = new ArrayList<>();
        army.getUnits().forEach(unit -> Units.add(new Unit(unit)));
        this.totalPower = army.getTotalPower();
        this.potentialPower = army.getPotentialPower();
    }
    public Army() {
        this.Units = new ArrayList<>();
        updateArmyStats();
    }
    public List<Unit> getUnits() {
        return Units;
    }
    public int getTotalPower() {
        return totalPower;
    }

    public int getTotalPowerPerUnit(String unitType) {
        return this.Units.stream().filter(unit->unit.getType()==unitType).mapToInt(Unit::getCurrentFirePower).sum();
    }
    public int getPotentialPower() {
        return potentialPower;
    }
    public void reduceCompetenceByPercent(double lossPercentage) {
        Optional.ofNullable(Units).orElse(Collections.emptyList()).forEach(unit -> unit.reduceCompetenceByPercent(lossPercentage));
        buryDeadUnits();
        updateArmyStats();
    }
    public void reduceCompetence() {
        Optional.ofNullable(Units).orElse(Collections.emptyList()).forEach(Unit::reduceCompetence);
        buryDeadUnits();
        updateArmyStats();
    }
    public void buryDeadUnits() {
        if(!Units.isEmpty()) {
            Iterator unit = Units.iterator();
            Unit deadUnit;
            while (unit.hasNext()) {
                deadUnit = (Unit) unit.next();
                if ("Dead".equals(deadUnit.getType()))
                {
                    unit.remove();
                }
            }
        }
    }
    public void calculatePotentialTotalPower() {
        this.potentialPower=
                Optional.ofNullable(Units).orElse(Collections.emptyList()).parallelStream()
                        .mapToInt(Unit::getMaxFirePower)
                        .sum();
    }
    public int calculateRehabilitationPrice() {
        double cost=0;
        if(Units.size() == 0) return 0;
        for(Unit unit:Units) {
            cost += unit.calculateRehabilitationPrice();
        }
        return (int)cost;
    }
    public int calculateRehabilitationPriceperUnit(String unitType) {
        double cost=0;
        if(Units.size() == 0) return 0;
        for(Unit unit:Units) {
            if (unit.getType() == unitType) {
                cost += unit.calculateRehabilitationPrice();
            }
        }
        return (int)cost;
    }

    public void destroyArmy() {
        Units = null;
        totalPower=potentialPower=0;
    }
    public void uniteArmies(Army SelectedArmyForce) {
        this.Units.addAll(SelectedArmyForce.Units);

        SelectedArmyForce.destroyArmy();
        updateArmyStats();
    }
    public void addUnit(Unit newUnit) {
        Units.add(newUnit);
        updateArmyStats();
    }
    public void updateArmyStats(){
        calculatePotentialTotalPower();
        calculateTotalPower();
    }
    public int getArmyValueInFunds() {
        return Optional.ofNullable(Units).orElse(Collections.emptyList()).stream()
                .mapToInt(Unit::getPurchase)
                .sum();
    }
    public void calculateTotalPower() {
        this.totalPower=
                Optional.ofNullable(Units).orElse(Collections.emptyList()).parallelStream()
                        .mapToInt(Unit::getCurrentFirePower)
                        .sum();
    }
    public void rehabilitateArmy(){
        Optional.ofNullable(Units).orElse(Collections.emptyList()).parallelStream().forEach(Unit::rehabilitateUnit);
        updateArmyStats();
    }
    public int howMachFromThisType(int amountUnits)
    {
        return ((int)this.Units.stream().filter(unit -> unit.getRank()==amountUnits).count());

    }
    public int howMachFromThisUnitType(String unitType)
    {
        return ((int)this.Units.stream().filter(unit -> unit.getType()==unitType).count());

    }
    public void killAllTheType(int sizeUnits)
    {
        this.Units.stream().filter(unit -> unit.getRank()==sizeUnits).peek(unit -> unit.setType("Dead"));
        buryDeadUnits();
    }

    public void killOnlyNumber(int sizeUnits, int howMachToKill)
    {
        int i=0;

        while(howMachToKill>0)
        {
            if(this.Units.get(i).getRank()==sizeUnits)
            {
                howMachToKill--;
                Units.get(i).setType("Dead");
            }
            i++;
        }
        buryDeadUnits();
    }
}
