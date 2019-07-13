package GameEngine;
import GameObjects.*;
import com.sun.istack.internal.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameManager implements Serializable {
    private int ID;
    public  int roundNumber=1;
    private static int gamesIDCounter = 0;
    private Stack<History> history;
    private GameDescriptor gameDescriptor;
    private Player currentPlayerTurn=null;
    private Army   selectedArmyForce=null;
    private Queue<Player> playersTurns;
    private Territory selectedTerritoryByPlayer=null;

    public GameManager(GameDescriptor gameDes) {
        ID = ++gamesIDCounter;
        gameDescriptor = gameDes;
        playersTurns = new ArrayBlockingQueue<>(gameDescriptor.getPlayersList().size());
        loadPlayersIntoQueueOfTurns();
        history = new Stack<>();
        history.push(new History(gameDescriptor,roundNumber));
    }

    public void setCurrentPlayerTurn(Player playerTurn)
    {
        currentPlayerTurn=playerTurn;
    }
    public void setSelectedTerritoryByPlayer(Territory territory)
    {
        selectedTerritoryByPlayer=territory;
    }
    public void setSelectedArmyForce(Army army)
    {
        selectedArmyForce=army;
    }
    public GameDescriptor getDescriptor() {
        return gameDescriptor;
    }

    public void setSelectedTerritoryForTurn(Territory selectedTerritory) {
        selectedTerritoryByPlayer = selectedTerritory;
    }
    public int getROundHistorySize()
    {
        return history.size();
    }

    public Army getSelectedArmyForce() {
        return selectedArmyForce;
    }
    public void buyUnits(Unit unit, int amount,boolean isItfirstUnit) { 
        int unitPrice;
        if(isItfirstUnit)
            selectedArmyForce = new Army();
        for(int i=0;i<amount;i++) {
            Unit unitToAdd = new Unit(unit.getType()
                    , unit.getRank()
                    , unit.getPurchase()
                    , unit.getMaxFirePower()
                    , unit.getCompetenceReduction());
            selectedArmyForce.addUnit(unitToAdd);
        }
        unitPrice= unit.getPurchase()*amount;
        currentPlayerTurn.decrementFunds(unitPrice);


    }


    public void transformSelectedArmyForceToSelectedTerritory() {
        selectedTerritoryByPlayer.getConquerArmyForce().uniteArmies(selectedArmyForce);
    }

    public void rehabilitateSelectedTerritoryArmy(){
        selectedTerritoryByPlayer.rehabilitateConquerArmy();
    }



    public void startOfRoundUpdates() {
        updateAllPlayersProductionStartOfRound();
        updateAllPlayerTerritoriesHold();
    }
    private void updateAllPlayerTerritoriesHold() {
        gameDescriptor.getPlayersList().forEach(this::updateTerritoriesHold);
    }
    private void updateAllPlayersProductionStartOfRound(){
        gameDescriptor.getPlayersList().forEach(this::harvestProduction);
    }


    public Stack<Map<Integer, Territory>> getMapsHistoryByOrder() {
        Stack<Map<Integer,Territory>> mapsHistoryByOrder = new Stack<>();
        history.forEach(history -> mapsHistoryByOrder.push(history.getMapList()));
        return mapsHistoryByOrder;
    }
    public void loadPlayersIntoQueueOfTurns() {
        if(gameDescriptor.getPlayersList() != null) {
            playersTurns.addAll(gameDescriptor.getPlayersList());
        }
        else
            System.out.println("NULL");
    }
    public void nextPlayerInTurn() {

        currentPlayerTurn= playersTurns.poll();

    }

    private void harvestProduction(Player player) {
        player.incrementFunds(calculatePotentialProduction(player));
    }
    public int calculatePotentialProduction(Player player) {
        return Optional.ofNullable(getTerritories(player))
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(Territory::getProfit).sum();
    }

    public void updateTerritoriesHold(Player player) {
        getTerritories(player).stream()
                .forEach(Territory::reduceCompetence);

        getTerritories(player).stream()
                .filter(Territory::isArmyTotalPowerUnderThreshold)
                .forEach(Territory::eliminateThisWeakArmy);
    }


    public void endOfRoundUpdates() {
        history.push(new History(gameDescriptor,++roundNumber));
        loadPlayersIntoQueueOfTurns();
    }



    public boolean attackConqueredTerritory() {
        boolean succeed;
        Battle.preparedToBattle(selectedTerritoryByPlayer.getConquerArmyForce(),selectedArmyForce,selectedTerritoryByPlayer);
        succeed = Battle.isAttackSucceed();
        if(succeed) {
            Battle.updateArmiesAfterAttackerVictory();
            selectedTerritoryByPlayer.setConquer(currentPlayerTurn);
            currentPlayerTurn.addTerritory(selectedTerritoryByPlayer);
        }
        else {
            Battle.updateArmiesAfterAttackerDefeat();
            selectedArmyForce = null;
        }
        if(Battle.isWinnerArmyNotStrongEnoughToHoldTerritory())
            selectedTerritoryByPlayer.xChangeFundsForUnitsAndHold();
        return succeed;
    }
    public int attackWellOrchestratedConqueredTerritory(int sizeUnits) {
        int succeed;
        Battle.preparedToBattle(selectedTerritoryByPlayer.getConquerArmyForce(),selectedArmyForce,selectedTerritoryByPlayer);
        succeed = Battle.iswellOrchestratedAttackSucceed(sizeUnits);
        if(succeed==1) //the attacker win
        {
            Battle.updateArmiesAfterAttackerwellOrchestratedVictory(sizeUnits);
            selectedTerritoryByPlayer.setConquer(currentPlayerTurn);
            currentPlayerTurn.addTerritory(selectedTerritoryByPlayer);
        }
        else {
            Battle.updateArmiesAfterAttackerwellOrchestratedDefeat(sizeUnits);
            selectedArmyForce = null;
        }
        if(Battle.isWinnerArmyNotStrongEnoughToHoldTerritory())
            selectedTerritoryByPlayer.xChangeFundsForUnitsAndHold();
        return succeed;
    }
    public boolean conquerNeutralTerritory() {
        boolean Flag=isSelectedArmyForceBigEnough();
        if (Flag) {
            currentPlayerTurn.addTerritory(selectedTerritoryByPlayer);
            selectedTerritoryByPlayer.setConquerArmyForce(selectedArmyForce);
            selectedTerritoryByPlayer.setConquer(currentPlayerTurn);

        }
        selectedArmyForce.destroyArmy();
        return Flag;
    }


    public int getFundsBeforeProduction() {
        //return currentPlayerTurn.getFunds();
        int currentHistoryPlayerlist= findIDFromDescriptor();

        return history.peek().getPlayerList().get(currentHistoryPlayerlist).getFunds();
    }

    private int findIDFromDescriptor() {

        return gameDescriptor.getPlayersList().indexOf(currentPlayerTurn);
    }

    public List<Territory> getCurrentPlayerTerritories() {
        return getTerritories(currentPlayerTurn);
    }
    private List<Territory> getTerritories(Player player) {
        List <Territory> playerTerritories=null;
        if(player.getTerritoriesID() != null) {
            playerTerritories= player.getTerritoriesID().stream()
                    .mapToInt(Integer::intValue)
                    .mapToObj(this::getTerritoryByID)
                    .collect(Collectors.toList());
        }
        return playerTerritories;
    }
    private Territory getTerritoryByID(int TerritoryID)
    {
        return gameDescriptor.getTerritoryMap().get(TerritoryID);
    }
    public int getCurrentPlayerTerritoriesAmount() {
        if(currentPlayerTurn.getTerritoriesID() == null)
            return 0;
        return currentPlayerTurn.getTerritoriesID().size();
    }

    @Nullable
    public Player getWinnerPlayer() {
        int winnerPlayerID=1,maxScore =0;
        int winnerPlayerOrder=5;
        int size =gameDescriptor.getPlayersList().size();
        int [] playerScores= new int [size];

        for (int i=0; i< size;i++) playerScores[i] = 0;
        int count=0;
        for(Player player: gameDescriptor.getPlayersList())
        {

            for(Integer territoryID:player.getTerritoriesID()) {
                playerScores[count]+= gameDescriptor.getTerritoryMap().get(territoryID).getProfit();
            }
            count++;
        }

        for (int i=0;i< size ; i++) {

            if(playerScores[i] >= maxScore) {
                maxScore = playerScores[i];
                winnerPlayerID = gameDescriptor.getPlayersList().get(i).getID();
                winnerPlayerOrder=i;
            }
        }

        for(int i=0;i< size;i++) {
            if(playerScores[i] == maxScore && winnerPlayerID != gameDescriptor.getPlayersList().get(i).getID())
                return null;
        }
        return gameDescriptor.getPlayersList().get(winnerPlayerOrder);
    }

    public int getRehabilitationArmyPriceInTerritory(Territory territory){
        return territory.getRehabilitationArmyPriceInTerritory();
    }



    private boolean isTargetTerritoryOneBlockAway() {
        for(Territory territory:getCurrentPlayerTerritories()) {
            if(Math.abs(territory.getID()-selectedTerritoryByPlayer.getID()) == 1) {
                int minID = Math.min(territory.getID(),selectedTerritoryByPlayer.getID());
                int maxID = Math.max(territory.getID(),selectedTerritoryByPlayer.getID());
                if(maxID % gameDescriptor.getColumns() == 0)
                    return true;
                if((minID % gameDescriptor.getColumns() != 0 )
                        && (minID / gameDescriptor.getColumns() == maxID / gameDescriptor.getColumns())) {
                    return true;
                }
            }
            else if(Math.abs(territory.getID()-selectedTerritoryByPlayer.getID()) == gameDescriptor.getColumns())
                return true;
        }
        return false;
    }

    public boolean isTargetTerritoryValid() {
        return isTargetTerritoryOneBlockAway();
    }

    private boolean isSelectedArmyForceBigEnough() {
         return selectedArmyForce.getTotalPower() >= selectedTerritoryByPlayer.getArmyThreshold();
    }

    public boolean isTerritoryBelongsCurrentPlayer() {
        if(selectedTerritoryByPlayer.getConquer() == null)
            return false;
        return selectedTerritoryByPlayer.getConquer().equals(currentPlayerTurn);
    }

    public boolean isConquered() {
        return selectedTerritoryByPlayer.isConquered();
    }

    public boolean isCycleOver(){
        return playersTurns.isEmpty();
    }

    public boolean isGameOver() {
        return gameDescriptor.getTotalCycles() < roundNumber;
    }

    public int getCurrentPlayerFunds(){return currentPlayerTurn.getFunds();}

    public boolean isSelectedPlayerHasEnoughMoney(Supplier <Integer> amountOfMoney) {
        return amountOfMoney.get() <= getCurrentPlayerFunds();
    }

    public Player getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public void roundUndo() {
        history.pop();
        updateGameDescriptorAfterUndo();
    }
    private void updateGameDescriptorAfterUndo() {
        roundNumber = history.peek().getTurn();
        gameDescriptor.setTerritoryMap(history.peek().getCopyOfMap());
        gameDescriptor.setPlayersList(history.peek().getCopyOfPlayersList());
        updateTurnsObjectQueue();
    }
    private void updateTurnsObjectQueue() {
        while (!playersTurns.isEmpty())
            playersTurns.poll();
        loadPlayersIntoQueueOfTurns();
    }
    public void selectedArmyForceDie()
    {
        selectedArmyForce=null;
    }


    public void deletePlayer()
    {
        history.peek().removePlayerFromHistorylist(currentPlayerTurn);
        playersTurns.remove(currentPlayerTurn);

    }
}
