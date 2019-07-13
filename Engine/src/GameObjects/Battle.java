package GameObjects;

import java.io.Serializable;
import java.util.Random;



public class Battle implements Serializable {
    private static Territory Territory=null;
    private static Army currentArmy=null,attackingArmy=null;

    public static void preparedToBattle(Army newConquerArmy,Army newAttackingArmy,Territory newBattleTerritory) {
        currentArmy=newConquerArmy;
        attackingArmy=newAttackingArmy;
        Territory=newBattleTerritory;
    }
    public static Boolean isWinnerArmyNotStrongEnoughToHoldTerritory() {
        return Territory.isArmyTotalPowerUnderThreshold();
    }
    public static boolean isAttackSucceed() {
        int totalArmiesForces = currentArmy.getTotalPower() + attackingArmy.getTotalPower();
        Random rand = new Random();
        int randomSide = rand.nextInt(totalArmiesForces) +1;
        return randomSide > currentArmy.getTotalPower();
    }
    public static void updateArmiesAfterAttackerVictory() {
        Territory.getConquer().getTerritoriesID().remove(new Integer(Territory.getID())); //Removes Defeated Conquer Army
        if(attackingArmy.getTotalPower() >= currentArmy.getTotalPower()) // Goliat effect
            attackingArmy.reduceCompetenceByPercent(1-((double)currentArmy.getTotalPower()) / ((double)attackingArmy.getTotalPower()));
        else attackingArmy.reduceCompetenceByPercent(0.5);
        Territory.setConquerArmyForce(attackingArmy);
    }
    public static void updateArmiesAfterAttackerDefeat() {
        if(attackingArmy.getTotalPower() <= currentArmy.getTotalPower())
            currentArmy.reduceCompetenceByPercent(1-((double)attackingArmy.getTotalPower()) / ((double)currentArmy.getTotalPower()));
        else currentArmy.reduceCompetenceByPercent(0.5);
    }
    public static int iswellOrchestratedAttackSucceed(int sizeUnits) {

            int attackerWon = 0;//0 if tie,1 if attack win,else 2
            int attackAmount;
            int defendAmount;
            while (attackerWon == 0 && sizeUnits >= 1)
            {

                defendAmount = Territory.getConquerArmyForce().howMachFromThisType(sizeUnits);
                attackAmount = attackingArmy.howMachFromThisType(sizeUnits);
                if (attackAmount > defendAmount) {
                    attackerWon = 1;
                }
                else if (attackAmount < defendAmount) {
                    attackerWon = 2;
                }
                else {
                    sizeUnits--;
                }
            }
            return attackerWon;


    }
    public static void updateArmiesAfterAttackerwellOrchestratedVictory(int sizeUnits)
    {
        int attackAmount;
        int defendAmount;
        int howMachToKill;
        while (sizeUnits>=1)
        {
            defendAmount = Territory.getConquerArmyForce().howMachFromThisType(sizeUnits);
            attackAmount = attackingArmy.howMachFromThisType(sizeUnits);

            howMachToKill=defendAmount;
            if (defendAmount>=attackAmount)
            {
                attackingArmy.killOnlyNumber(sizeUnits,attackAmount);
            }
            else
            {
                attackingArmy.killOnlyNumber(sizeUnits,howMachToKill);
            }
            sizeUnits--;
        }
        attackingArmy.calculateTotalPower();
        attackingArmy.calculatePotentialTotalPower();
        Territory.setConquerArmyForce(attackingArmy);

    }
    public static void updateArmiesAfterAttackerwellOrchestratedDefeat(int sizeUnits)
    {
        int attackAmount=0;
        int defendAmount;
        int howMachToKill;

        while (sizeUnits>=1)
        {
            defendAmount = Territory.getConquerArmyForce().howMachFromThisType(sizeUnits);
            attackAmount = attackingArmy.howMachFromThisType(sizeUnits);
            howMachToKill=attackAmount;
            if (defendAmount<=attackAmount)
            {
                currentArmy.killOnlyNumber(sizeUnits,defendAmount);
            }
            else
            {
                currentArmy.killOnlyNumber(sizeUnits,attackAmount);
            }
            sizeUnits--;
        }
        currentArmy.calculatePotentialTotalPower();
        currentArmy.calculateTotalPower();
    }



}