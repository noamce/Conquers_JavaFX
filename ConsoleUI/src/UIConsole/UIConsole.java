package UIConsole;

import GameEngine.GameDescriptor;
import GameEngine.GameEngine;
import GameObjects.Player;
import GameObjects.Territory;
import Generated.Game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;

public class UIConsole {
    private String lastKnownXmlPath;
    private boolean gameStarted , gameHasBeenPlayed;
    private static final String SOLDIER = "Soldier";
    private GameEngine engine;
    public UIConsole() {
        this.engine = new GameEngine();
    }
    public void showMainMenu() {
        gameStarted = false;
        int selection;
        String xmlPath, savePath , loadPath;
        System.out.println("Conquers the GAME!");
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Menu:");
            System.out.println("1.Load XML File"
                    +"\n"
                    + "2.Start game "
                    + "\n"
                    + "3.Show game stats."
                    + "\n"
                    + "4.Start round"
                    +"\n"
                    + "5.Game history."
                    + "\n" 
                    + "6.Exit."
                    + "\n"
                    + "7.Save game."
                    + "\n"
                    + "8.Load game."
                    + "\n"
                    + "9.Undo round");
            try{
                selection = sc.nextInt();
                switch (selection) {
                    case 1: //Load XML
                        if (gameStarted)
                            System.out.println("The game is loaded already");
                        else {
                            System.out.println("Please enter the full XML path ");
                            Scanner pathScanner = new Scanner(System.in);
                            xmlPath = pathScanner.nextLine();
                            engine.loadXML(xmlPath);
                            if (GameEngine.flag == 1) {
                                System.out.println("XML is Loaded ");
                                lastKnownXmlPath = xmlPath;
                                gameHasBeenPlayed = false;
                            }
                        }
                        break;
                    case 2: // Start game
                        if (GameEngine.flag != 1) {
                            System.out.println("No game has been loaded , please load a game");
                            break;
                        } else if (!gameStarted) {
                            System.out.println("Start to Play");
                            gameStarted = true;
                            if (gameHasBeenPlayed)
                                engine.loadXML(engine.getDescriptor().getLastKnownGoodString());
                            engine.newGame();
                            roundManager();
                        } else
                            System.out.println("The game is still on");
                        break;
                    case 3: // Show game stats
                        if (!gameStarted)
                            System.out.println("Please start the game to this action");
                        else
                            showGameStats();
                        break;
                    case 4: // 4.Start round
                        if (!gameStarted)
                            System.out.println("Please start the game to this action");
                        else {
                            System.out.println("Round begin");
                            roundManager();
                        }
                        break;
                    case 5: // Game history
                        if (gameStarted)
                            printHistory();
                        break;
                    case 6: // Exit
                        System.exit(1);
                    case 7: // Save game
                         if (gameStarted) {
                        System.out.println("Enter a name for this save game:");
                        Scanner pathScanner = new Scanner(System.in);
                        savePath = pathScanner.nextLine();
                        GameEngine.saveGame(Paths.get(savePath), GameEngine.gameManager);
                } else
                System.out.println("No game is found to save.");
                        break;
                    case 8: //Load game
                        if(!gameStarted) {
                            System.out.println("Enter the path for the saved game:");
                            Scanner loadScanner = new Scanner(System.in);
                            loadPath = loadScanner.nextLine();
                            Path load = engine.getLoadFilePath(loadPath);
                            if (engine.loadGame(load)) {
                                gameStarted = true;
                                engine.setDescriptor(GameEngine.gameManager.getDescriptor());
                            }
                        }
                        break;
                    case 9: //Undo round
                        if(gameStarted)
                            undoRound();
                        else
                            System.out.println("No game started to undo round.");
                        break;
                }


            }
            catch(InputMismatchException e){
                System.out.println("Please choose a valid option");
                sc.next();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GameDescriptor.IllegalunitdetailsorderException e) {
                e.printStackTrace();
            } catch (GameDescriptor.IllegalunitdetailsorNamedubbException e) {
                e.printStackTrace();
            } catch (GameDescriptor.noDefaultProfitException e) {
                e.printStackTrace();
            } catch (GameDescriptor.dubbPlayernameException e) {
                e.printStackTrace();
            } catch (GameDescriptor.dubbIDException e) {
                e.printStackTrace();
            } catch (GameDescriptor.noDefaultThresholdException e) {
                e.printStackTrace();
            } catch (GameDescriptor.dubbPlayerIdException e) {
                e.printStackTrace();
            } catch (GameDescriptor.colandRowException e) {
                e.printStackTrace();
            } catch (GameDescriptor.IllegalDupprankortypeException e) {
                e.printStackTrace();
            }
        }
    }
    private void chooseTerritoryIfNone() {
        System.out.println(GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name()
                + " have no territories to show"
                + "\n");
        drawMap();
        System.out.println("Please select a territory: ");
        Scanner sc = new Scanner(System.in);
        int territoryID = sc.nextInt();
        while(!engine.getDescriptor().getTerritoryMap().containsKey(territoryID)) {
            System.out.println("Enter a valid territory ID as shown.");
            territoryID = sc.nextInt();
        }
        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryID);
        GameEngine.gameManager.setSelectedTerritoryForTurn(targetTerritory);
        if(targetTerritory.isConquered()) {
            attackConqueredTerritoryResult(targetTerritory);
        }else {
            getNeutralTerritory(targetTerritory);
        }
    }
    private void actOnTerritory() {
        if(!GameEngine.gameManager.getCurrentPlayerTerritories().isEmpty()) {
            int  territoryID;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select a territory ID to make a move: ");
            territoryID = scanner.nextInt();
            while(!checkTerritoryIdValid(territoryID)) {
                System.out.println("Please Enter a valid territory ID.");
                territoryID = scanner.nextInt();
            }
            Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryID);
            GameEngine.gameManager.setSelectedTerritoryForTurn(targetTerritory);
            if(GameEngine.gameManager.isTerritoryBelongsCurrentPlayer()) {
                actOnSelfTerritory(territoryID, scanner, targetTerritory);
            }
            else
            {
                if(GameEngine.gameManager.isConquered())
                {
                    if(GameEngine.gameManager.isTargetTerritoryValid())
                    {
                        attackConqueredTerritoryResult(targetTerritory);
                    }
                    else
                    {
                        System.out.println("Invalid territory");
                    }
                }
                else {
                    if(GameEngine.gameManager.isTargetTerritoryValid()){
                        getNeutralTerritory(targetTerritory);
                    }
                    else {
                        System.out.println("Invalid territory");
                    }
                }
            }
        } else {
            chooseTerritoryIfNone();
        }
    }
    private void actOnSelfTerritory(int territoryID, Scanner scanner, Territory targetTerritory) {
        int choice;
        System.out.println("Territory number:  "
                + targetTerritory.getID()
                + "\n"
                + "Choose one of the following:"
                + "\n"
                + "1.Maintenance your army"
                + "\n"
                + "2.Add army force");
        choice = scanner.nextInt();
        switch (choice)
        {
            case 1:
                Supplier<Integer> enoughMoney = () -> GameEngine.gameManager.getRehabilitationArmyPriceInTerritory(targetTerritory);
                if(GameEngine.gameManager.isSelectedPlayerHasEnoughMoney(enoughMoney)) {
                    System.out.println("Maintenance army on territory ID: " + territoryID );
                    GameEngine.gameManager.rehabilitateSelectedTerritoryArmy();
                }
                else
                    System.out.println("You dont have enough Turings");
                break;
            case 2:
                printAndBuySelectedUnit();
                GameEngine.gameManager.transformSelectedArmyForceToSelectedTerritory();
                break;
        }
    }
    private void getNeutralTerritory(Territory targetTerritory) {
        System.out.println("Territory threshold: "
                + targetTerritory.getArmyThreshold()
                +"\n" + "Turings you have "
                + GameEngine.gameManager.getCurrentPlayerFunds());
        printAndBuySelectedUnit();
        if(GameEngine.gameManager.conquerNeutralTerritory())
            System.out.println("Territory : "
                    + targetTerritory.getID()
                    + " is now conquered by "
                    + GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name()+ "\n");
        else
            System.out.println("The amount of army is not enough ,you cant Conquer");
    }

    private void buyUnits(int amount,String unitType,boolean isItfirstUnit) {


       GameEngine.gameManager.buyUnits(engine.getDescriptor().getUnitMap().get(unitType) , amount,isItfirstUnit);
    }
    private void printAndBuySelectedUnit(){
        Scanner scanner = new Scanner(System.in);
        int howManyToAdd, selection;
        System.out.println("Select how many units you want from each Type:");
        List<String> l = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());
        int HowManyUnitTypes=engine.getDescriptor().getUnitMap().size();
        for(int i=0;i< HowManyUnitTypes;i++)
        {
            String unitType = null;
            unitType=l.get(i);
            System.out.println( unitType + " have "+engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower()+" fire power each");
            System.out.println( unitType + " cost "+engine.getDescriptor().getUnitMap().get(unitType).getPurchase()+" each");
            System.out.println("How many " + unitType + " you want to purchase?");
            howManyToAdd = scanner.nextInt();
            while(howManyToAdd*engine.getDescriptor().getUnitMap().get(unitType).getPurchase() > GameEngine.gameManager.getCurrentPlayerFunds()){ //must enter a valid amount to purchase
                System.out.println("Not enough Turings , try again ");
                howManyToAdd = scanner.nextInt();
                if(howManyToAdd == 0)
                    break;
            }
            buyUnits(howManyToAdd, unitType,0==i);
        }
    }
    private void showPlayerTurnOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose one of the following:"
                + "\n"
                + "1.End turn."
                + "\n"
                + "2.Act on a territory.");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                break;
            case 2:
                if(!GameEngine.gameManager.getCurrentPlayerTerritories().isEmpty()) {
                    drawMap();
                }
                actOnTerritory();
                break;
        }
    }
    private void firstRound() {
        showCurrentPlayerStats();
        chooseTerritoryIfNone();
    }
    private void drawMap() {
        Player player3=null,player4=null;
        int size;
        size=engine.getDescriptor().getPlayersList().size();
        if(size == 3)
            player3=engine.getDescriptor().getPlayersList().get(2);
        if(size == 4)
        {
            player4=engine.getDescriptor().getPlayersList().get(3);
        }

        Board map = new Board(engine.getDescriptor().getTerritoryMap()
                ,engine.getDescriptor().getRows()
                ,engine.getDescriptor().getColumns()
                ,engine.getDescriptor().getPlayersList().get(0)
                ,engine.getDescriptor().getPlayersList().get(1),player3,player4);
        map.PrintBoardTable();
        map.PrintDataTable();
    }
    private void getGameWinner() {
        Player winner = GameEngine.gameManager.getWinnerPlayer();
        if(winner == null){
            System.out.println("its a draw");
        } else {
            System.out.println("The game is over");
            System.out.println("The winning player is: " + winner.getPlayer_name());
            System.out.println("Game has ended" + "\n");

        }
    }
    private void printFundsBeforeProduction() {
        System.out.println("Turings you have before harvest: "
                + GameEngine.gameManager.getFundsBeforeProduction());

    }
    private void printFunds() {
        System.out.println("Turings after harvest: "+ GameEngine.gameManager.getCurrentPlayerFunds());
    }
    private void printHistory() {

        GameEngine.gameManager.getMapsHistoryByOrder().stream()
                .forEach(this::drawMap);
    }

    private void drawMap(Map<Integer,Territory> historyMap) {
        Player player3=null,player4=null;
        int size;
        size=engine.getDescriptor().getPlayersList().size();
        if(size == 3)
          player3=engine.getDescriptor().getPlayersList().get(2);
        if(size == 4)
        {
            player4=engine.getDescriptor().getPlayersList().get(3);
        }


        Board map = new Board(historyMap
                ,engine.getDescriptor().getRows()
                ,engine.getDescriptor().getColumns()
                ,engine.getDescriptor().getPlayersList().get(0)
                ,engine.getDescriptor().getPlayersList().get(1),player3,player4);
        map.PrintBoardTable();
        map.PrintDataTable();
    }
    private void showRoundStats() {
        System.out.println("Cycle: " + (GameEngine.gameManager.roundNumber)+ "/"
                + engine.getDescriptor().getTotalCycles()
                + "\n");
    }
    private void showCurrentPlayerStats(){
        showCurrentPlayerTerritoriesStats();
    }
    private void showAllPlayersStats() {
        System.out.println("Players status: " + "\n");
        engine.getDescriptor().getPlayersList().parallelStream()
                .forEach(player -> System.out.println("Player: "
                        + player.getPlayer_name()
                        + "\n" + "ID: "
                        + player.getID() + "\n"
                        + "Amount of territories: "
                        + player.getTerritoriesID().size()
                        + "\n"
                        + "Turings: "
                        + player.getFunds()));
        System.out.println("\n");
    }
    private void showGameStats(){
        System.out.println("Game stats:");
        drawMap();
        showRoundStats();
        showAllPlayersStats();
    }
    private void attackConqueredTerritoryResult(Territory targetTerritory) {

        boolean flag=true;
        while(flag) {
            System.out.println("Attacking territory ID: " + targetTerritory.getID());
            //what kind of attack?
            System.out.println("Choose what kind of attack you want to use:");
            System.out.println("1.Calculated Risk");
            System.out.println("2.A well-orchestrated attack");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice == 1) {
                CalculatedRisk(targetTerritory);
                flag=false;
            }
            if (choice == 2) {
                wellOrchestratedAttack(targetTerritory);
                flag=false;
            } else
                if(flag)
                flag=true;
        }
    }
    private boolean checkTerritoryIdValid(int territoryID) {
        return engine.getDescriptor().getTerritoryMap().containsKey(territoryID);
    }
    private void printArmyOnTerritory(Territory targetTerritory) {
        System.out.println("This are the territory stats: ");//add id territory per print
        System.out.println("Territory Id: "+ targetTerritory.getID());
        System.out.println("Unit distribution:");
        targetTerritory.getConquerArmyForce().getUnits().forEach(unit -> System.out.println("Type: "
                + unit.getType()
                + " Firepower: "
                + unit.getCurrentFirePower()
                + " Competence reduction: "
                + unit.getCompetenceReduction()));
        System.out.println("\n\n");
    }
    private void showCurrentPlayerTerritoriesStats() {
        System.out.println(GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name() + "\n"+"Territories: " + GameEngine.gameManager.getCurrentPlayerTerritoriesAmount());
        GameEngine.gameManager.getCurrentPlayerTerritories().forEach(territory ->
                System.out.println("Territory ID: " + territory.getID()
                        + "\n" + " Total Fire power: "
                        + territory.getConquerArmyForce().getTotalPower()
                        + "\n"
                        + "Army threshold: "
                        + territory.getArmyThreshold()
                        + "\n"
                        + "Current units: "
                        + "\n"
                        + "Soldier"
                        + ", Amount: "
                        + territory.getConquerArmyForce().getUnits().size()
                        + "\n" + "Fire Power: "
                        + territory.getConquerArmyForce().getTotalPower()
                        + "\n"
                        + "Full Maintenance cost: "
                        + GameEngine.gameManager.getRehabilitationArmyPriceInTerritory(territory)
                        + "\n"+"\n"
                        +"\n"));


        GameEngine.gameManager.getCurrentPlayerTerritories().forEach(territory -> printArmyOnTerritory(territory));
    }
    private void startTurn() {
        System.out.println("Cycle: " + GameEngine.gameManager.roundNumber + "/"+ engine.getDescriptor().getTotalCycles());
        System.out.println("Its  " +GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name()+" turn");
        printFundsBeforeProduction();
        printFunds();
        showCurrentPlayerStats();
    }
    private void roundManager(){

        GameEngine.gameManager.startOfRoundUpdates();
        while(!GameEngine.gameManager.isCycleOver()) {
            GameEngine.gameManager.nextPlayerInTurn();
            if(GameEngine.gameManager.roundNumber == 1 && GameEngine.gameManager.getCurrentPlayerTerritories().isEmpty()) {
                firstRound();
            } else
                {

                startTurn();
                showPlayerTurnOptions();
                printFunds();
                showCurrentPlayerTerritoriesStats();

            }
        }
        System.out.println("Round ended" + "\n");
        GameEngine.gameManager.endOfRoundUpdates();
        //suspecthere
        if(GameEngine.gameManager.isGameOver()) {
            getGameWinner();
            gameStarted = false;
            gameHasBeenPlayed = true;
            GameEngine.gameManager = null;
        }
    }
    private void undoRound() {
        if ((GameEngine.gameManager.getROundHistorySize()>1))
        {
            GameEngine.gameManager.roundUndo();
        }
        else
            System.out.println("Undo is not possible at first round.");
    }

    private void CalculatedRisk(Territory targetTerritory)
    {
        printAndBuySelectedUnit();
        int defendingArmyAmountOfUnits = targetTerritory.getConquerArmyForce().getUnits().size();
        boolean attackerWon = GameEngine.gameManager.attackConqueredTerritory();
        if(attackerWon) {
            System.out.println("Attacker Won"
                    + "\n"
                    +GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name()+ " have conquered territory number: " + targetTerritory.getID());
            if(targetTerritory.getConquerArmyForce()== null) {
                System.out.println("Army is not above threshold ");
            }
            else printArmyOnTerritory(targetTerritory);
        }
        else
            System.out.println("Attacker lost");
        System.out.println("The defending units amount is "
                + defendingArmyAmountOfUnits
                + " Units!"
                + "\n");
    }

    private void wellOrchestratedAttack(Territory targetTerritory)
    {
        printAndBuySelectedUnit();
        int attackerWon = GameEngine.gameManager.attackWellOrchestratedConqueredTerritory(engine.getDescriptor().getUnitMap().size());
        if(attackerWon==1)
        {
            System.out.println("Attacker win" + "\n"
                    +GameEngine.gameManager.getCurrentPlayerTurn().getPlayer_name()+ " have conquered territory number: " + targetTerritory.getID());
            if(targetTerritory.getConquerArmyForce()== null) {
                System.out.println("Army is not above threshold ");
            }
            else printArmyOnTerritory(targetTerritory);
        }
        else
            System.out.println("Attacker lost");
        System.out.println("The defending units amount is "
                + targetTerritory.getConquerArmyForce().getUnits().size()
                + " Units!"
                + "\n");
    }



    public void quitGame()
    {
        engine.deletePlayer();




    }


}
