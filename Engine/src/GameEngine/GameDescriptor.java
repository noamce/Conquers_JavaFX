package GameEngine;
import GameObjects.Player;
import GameObjects.Territory;
import GameObjects.Unit;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;

public class GameDescriptor implements Serializable {
    private int initialFunds , totalCycles , columns , rows;
    private int defaultThreshold , defaultProfit;
    private Map<Integer,Territory> territoryMap;
    private Map<String , Unit> unitMap;
    private List<Player> playersList;
    private String gameType;
    private String lastKnownGoodString;

    public GameDescriptor(Path xmlPath) throws dubbPlayernameException, dubbPlayerIdException, IllegalDupprankortypeException, IllegalunitdetailsorNamedubbException, IllegalunitdetailsorderException, dubbIDException, noDefaultProfitException, noDefaultThresholdException, colandRowException {
        Generated.GameDescriptor descriptor = null;
        try {
            descriptor = deserializeFrom(xmlPath);
        } catch (JAXBException ignored) { }

        if(descriptor == null)
            throw new IllegalArgumentException();
        lastKnownGoodString = xmlPath.toString();
        getGameStats(descriptor);
        this.territoryMap = buildTerritoryMap(descriptor);
        if(checkRowsAndColumns() && validateTerritories(descriptor)){
                try {
                    this.unitMap = loadUnitsDescription(descriptor);
                    this.playersList = addPlayers(descriptor);

                }catch (IllegalArgumentException l)
                {

                    throw new IllegalArgumentException();
                }


        }
        else {
            throw new IllegalArgumentException();
        }
    }


    public String getLastKnownGoodString() {
        return lastKnownGoodString;
    }
    public int getTotalCycles() {
        return totalCycles;
    }
    public int getColumns() {
        return columns;
    }
    public int getRows() {
        return rows;
    }
    public Map<Integer, Territory> getTerritoryMap() { return territoryMap; }
    public Map<String,Unit> getUnitMap() {
        return unitMap;
    }
    public List<Player> getPlayersList() { return playersList; }
    public void setTerritoryMap(Map<Integer, Territory> territoryMap) {
        this.territoryMap = territoryMap;
    }



    public void setPlayersList(List<Player> playersList) {
        this.playersList = playersList;
    }



    private List<Player> addPlayers(Generated.GameDescriptor descriptor) throws IllegalArgumentException, dubbPlayernameException, dubbPlayerIdException {


        List<Player> players= new ArrayList<>();
        List<Generated.Player> playersDes =loadplayers(descriptor);
        if(playersDes.size()>4 || playersDes.size()<2) {
            System.out.println("Error in the Players details of the XML Number of Players is not Valid");
            throw new IllegalArgumentException();

        }
        else {
            int count=0;
            for (Generated.Player player : playersDes) {
                int id;
                String name;
                id = player.getId().intValue();
                name = player.getName();

                Player newPlayer = new Player(id, name, initialFunds,++count);
                players.add(newPlayer);

            }
            notValidateNameAndId(players);


        }


        return players;
    }

    private Boolean notValidateNameAndId(List<Player> players) throws dubbPlayernameException, dubbPlayerIdException {

        List<Integer> idCheck=new ArrayList<>();
        List<String> nameCheck=new ArrayList<>();
        for( Player player : players){
            idCheck.add(player.getID());
            nameCheck.add(player.getPlayer_name());
        }
        Collections.sort(idCheck);
        Collections.sort(nameCheck);
        for(int i=0;i<idCheck.size();i++) {

            if (i < idCheck.size() - 1) {
                    if (idCheck.get(i).intValue() == idCheck.get(i + 1).intValue())
                    {
                        System.out.println("Error in the Players details of the XML are at least one ID that showing twice");
                        throw new dubbPlayerIdException();
                    }

                    if (nameCheck.get(i).equals(nameCheck.get(i + 1)))
                    {
                        System.out.println("Error in the Players details of the XML names are at least one name that showing twice");
                        throw new dubbPlayernameException();
                    }

                }

        }
        return false;
    }

    private List<Generated.Player> loadplayers(Generated.GameDescriptor descriptor) {
        return descriptor.getPlayers().getPlayer();
    }

    private boolean checkRowsAndColumns() throws colandRowException {
        if((columns >= 3 && columns <= 30) && (rows <= 30 && rows >= 2))
            return true;
        else{
           throw new colandRowException();
        }

    }

    public Map<Integer,Territory> buildTerritoryMap(Generated.GameDescriptor descriptor) {




        List<Generated.Teritory> territoryList = loadTerritories(descriptor);
        Map<Integer, Territory> territoriesMap = new HashMap<>();
        if(territoryList != null) {
            for(int i = 1; i <= columns * rows ; i++) {
               for(int j = 0 ; j < territoryList.size() ; j++) {
                   if(territoryList.get(j).getId().intValue() == i) {
                       createTerritoryToMap(j , territoriesMap , i , territoryList);
                       break;
                   }
                   else
                       createTerritoryToMapFromDefault(territoriesMap, i);
               }
            }
        }

        return territoriesMap;
    }



    private void createTerritoryToMap(int j, Map<Integer, Territory> territoriesMap, int i, List<Generated.Teritory> territoryList)
    {
        int profit;
        int armyThreshold;
        profit = territoryList.get(j).getProfit().intValue();
        armyThreshold = territoryList.get(j).getArmyThreshold().intValue();
        Territory newTerritory = new Territory(i , profit , armyThreshold);
        territoriesMap.put(newTerritory.getID() , newTerritory);
    }
    private List<Generated.Teritory> loadTerritories(Generated.GameDescriptor descriptor) {

        return descriptor.getGame().getTerritories().getTeritory();
    }

    public void getGameStats(Generated.GameDescriptor descriptor) {
        this.initialFunds = descriptor.getGame().getInitialFunds().intValue();
        this.totalCycles = descriptor.getGame().getTotalCycles().intValue();
        this.columns = descriptor.getGame().getBoard().getColumns().intValue();
        this.rows = descriptor.getGame().getBoard().getRows().intValue();
        this.gameType = descriptor.getGameType();
        if(descriptor.getGame().getTerritories().getDefaultArmyThreshold() != null)
        {
            this.defaultProfit = descriptor.getGame().getTerritories().getDefaultProfit().intValue();
        }
        if(descriptor.getGame().getTerritories().getDefaultArmyThreshold() != null) {
            this.defaultThreshold = descriptor.getGame().getTerritories().getDefaultArmyThreshold().intValue();
        }
    }
    private static Generated.GameDescriptor deserializeFrom(Path path) throws JAXBException {
        File file = new File(path.toString());
        JAXBContext jc = JAXBContext.newInstance(Generated.GameDescriptor.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (Generated.GameDescriptor) u.unmarshal(file);
    }

    private boolean validateTerritories(Generated.GameDescriptor descriptor) throws noDefaultThresholdException, noDefaultProfitException, dubbIDException {
        for(int i = 0; i < descriptor.getGame().getTerritories().getTeritory().size() - 1 ; i++) {
            if(descriptor.getGame().getTerritories().getTeritory().get(i).getId().equals(descriptor.getGame().getTerritories().getTeritory().get(i + 1).getId()))  {
                System.out.println("Double ID in xml detected , please try again.");
                throw new dubbIDException();
            }
        }
        return validateTerritoryDefaults(descriptor);
    }
    private boolean validateTerritoryDefaults(Generated.GameDescriptor descriptor) throws noDefaultThresholdException, noDefaultProfitException {
        if(descriptor.getGame().getTerritories().getDefaultProfit() == null && descriptor.getGame().getTerritories().getTeritory().size() != territoryMap.size()) {
            System.out.println("No default profit  in xml  try again");
            throw new noDefaultProfitException();
        }
        if(descriptor.getGame().getTerritories().getDefaultArmyThreshold() == null && descriptor.getGame().getTerritories().getTeritory().size() != territoryMap.size()) {
            System.out.println("No default army threshold  in xmltry again");
            throw new noDefaultThresholdException();
        }
        return true;
    }
    public Map<String , Unit> loadUnitsDescription(Generated.GameDescriptor descriptor) throws IllegalArgumentException, IllegalDupprankortypeException, IllegalunitdetailsorderException, IllegalunitdetailsorNamedubbException {
        Map<String , Unit> unitsMap = new HashMap<>();
        List<Generated.Unit> units = descriptor.getGame().getArmy().getUnit();
        for(Generated.Unit unit : units) {
            String type;
            int purchaseCost, maxFire, compReduction, rank;

            type = unit.getType();
            purchaseCost = unit.getPurchase().intValue();
            maxFire = unit.getMaxFirePower().intValue();
            compReduction = unit.getCompetenceReduction().intValue();
            rank = unit.getRank();


            Unit newUnit = new Unit(type, rank, purchaseCost, maxFire, compReduction);
            unitsMap.put(type, newUnit);

        }
        List<Integer> ranksCheck=new ArrayList<>();
        List<String> nameCheck=new ArrayList<>();
        for( Unit unit1 : unitsMap.values()){
            ranksCheck.add(unit1.getRank());
            nameCheck.add(unit1.getType());
        }
        Collections.sort(ranksCheck);
        Collections.sort(nameCheck);
        if(nameCheck.size() != units.size() || ranksCheck.size() !=units.size())
        {
            System.out.println("Error in the unit details of the XML There are duplicate of Rank or Type to the units");
            throw new IllegalDupprankortypeException();
        }
        for(int i=0;i<ranksCheck.size();i++) {

            if (ranksCheck.get(i).intValue() > units.size() || ranksCheck.get(i).intValue() < 1) {
                System.out.println("Error in the unit details of the XML Id's order are not right");
                throw new IllegalunitdetailsorderException();
            }
            else {
                if (i < ranksCheck.size() - 1)
                    if (ranksCheck.get(i).intValue() == ranksCheck.get(i + 1).intValue()|| nameCheck.get(i).equals(nameCheck.get(i+1))) {
                        System.out.println("Error in the unit details of the XML ID or Name is showing more than 1");
                        throw new IllegalunitdetailsorNamedubbException();
                    }
              }
            }





        return unitsMap;
    }
    private void createTerritoryToMapFromDefault(Map<Integer,Territory> territoriesMap, int i) {
        Territory newTerritory = new Territory(i , defaultProfit , defaultThreshold);
        territoriesMap.put(newTerritory.getID() , newTerritory);
    }


    public void deletePlayerDescriptor() {
        for(Territory territory : territoryMap.values())
        {
            territory.RemoveFromTerritory(GameEngine.gameManager.getCurrentPlayerTurn().getID());
        }

        playersList.remove(GameEngine.gameManager.getCurrentPlayerTurn());


    }

    private class IllegalnameandidException extends Throwable {


    }

    public class IllegalDupprankortypeException extends Throwable {
    }

    public class IllegalunitdetailsorNamedubbException extends Throwable {
    }

    public class IllegalunitdetailsorderException extends Throwable {
    }

    public class noDefaultThresholdException extends Throwable {
    }

    public class noDefaultProfitException extends Throwable {
    }

    public class dubbIDException extends Throwable {
    }

    public class dubbPlayernameException extends Throwable {
    }

    public class dubbPlayerIdException extends Throwable {
    }

    public class colandRowException extends Throwable {
    }
}
