package JavaFxUI;

import GameEngine.GameDescriptor;
import GameEngine.GameEngine;
import GameObjects.*;
import javafx.animation.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;



public class Controller implements Initializable {
    private GameUI UI;
    private GameEngine engine = new GameEngine();
    boolean gameStarted = false;
    VBox currentTerritoryBox = new VBox();
    int territoryId;
    int option = 0;//1=maintaince,2=calculatedRisk 3=well-orchestrated ,4=natural Territory,5=add army
    int animation1=1;//1=with animation,0=without.
    int i = 1;


    @FXML
    private BorderPane bigBorderPane;

    @FXML
    private ToolBar ToolBar;

    @FXML
    private ChoiceBox<String> selectStyleChoiceBox;

    @FXML
    private ChoiceBox<String> selectAnimationCoiceBox;

    @FXML
    private AnchorPane data_anchor;

    @FXML
    private Button saveGame;

    @FXML
    private Button load_game;

    @FXML
    private AnchorPane fullBoard;

    @FXML
    private Button LoadXML;

    @FXML
    private Button startGame;

    @FXML
    private GridPane board;

    @FXML
    private Pane detail;

    @FXML
    private HBox HboxDetail;

    @FXML
    private VBox armyDetail;

    @FXML
    private Label territory_id;

    @FXML
    private Label army;

    @FXML
    private Label PlayerNameLabel;

    @FXML
    private Label PlayerIdLabel;


    @FXML
    private Label TuringsLabel;


    @FXML
    private Label NumberofTerritoriesLabel;

    @FXML
    private Label playerColorLabel;


    @FXML
    private TableView armyTablePerTerritory;


    @FXML
    private TableColumn<Person1, String> typePerTerritory;

    @FXML
    private TableColumn<Person1, Integer> amountPerTerritory;

    @FXML
    private TableColumn<Person1, Integer> fpPerTerritory;

    @FXML
    private TableColumn<Person1, Integer> RankArmy;


    @FXML
    private TableColumn<Person1, Integer> maintenancePerTerritory;

    @FXML
    private VBox vboxData;

    @FXML
    private Pane paneData;


    @FXML
    private ScrollPane mapBorder;

    @FXML
    private Label player_name;

    @FXML
    private Label player_color;

    @FXML
    private Label turings;

    @FXML
    private Label number_of_territories;

    @FXML
    private Label player_id;

    @FXML
    private Label Round;

    @FXML
    private VBox options;

    @FXML
    private Button skip_turn;

    @FXML
    private Button ChangeStyle;

    @FXML
    private Button maintenance;

    @FXML
    private Button calculated_risk;

    @FXML
    private Button well_orchestrated;

    @FXML
    private Button Natural_Territory;

    @FXML
    private Button add_army;


    @FXML
    private Button retire;

    @FXML
    private TableView tableData;

    @FXML
    private TableColumn<Person, String> type;

    @FXML
    private TableColumn<Person, Integer> fp;

    @FXML
    private TableColumn<Person, TextField> amount;

    @FXML
    private TableColumn<Person, Integer> price;

    @FXML
    private TableColumn<Person, Integer> Subduction;

    @FXML
    private TableColumn<Person, Integer> rank;

    @FXML
    private Button confirm;

    @FXML
    private ScrollPane scrollPaneright;

    @FXML
    private Label price_to_maintenance;

    @FXML
    private Label Total_fire_power;
    @FXML
    private VBox warZone;

    @FXML
    private TableView A_table_war;

    @FXML
    private TableColumn<popUpdata, String> A_unit_war;

    @FXML
    private TableColumn<popUpdata, Integer> A_fp_war;

    @FXML
    private TableView D_table_war;

    @FXML
    private TableColumn<popUpdata, String> D_unit_war;

    @FXML
    private TableColumn<popUpdata, Integer> D_fp_war;
    @FXML
    private TableColumn<popUpdata, Integer> A_hm_war;
    @FXML
    private TableColumn<popUpdata, Integer> D_hm_war;

    @FXML
    private Label winner_war;
    @FXML
    private Button endGame;

    @FXML
    void endGame_onAction(ActionEvent event) throws InterruptedException {
        //pop-winner!!
        if (getWinnerPlayer()!=null) {
            warZone.setVisible(true);
            winner_war.setText("The Winner of the game: " + getWinnerPlayer().getPlayer_name());
            winner();
        }
        else {
            warZone.setVisible(true);
            winner_war.setText("            TIE!");
            winner_war.setFont(new Font("System Bold", 28));
            winner_war.setStyle("-fx-text-fill: #b210b3");
        }
            skip_turn.setDisable(true);
            startGame.setDisable(true);
            turnOffButtons();
            retire.setDisable(true);
            load_game.setDisable(false);
            LoadXML.setDisable(false);
            saveGame.setDisable(true);
            engine.getDescriptor().getPlayersList().clear();


    }


    private void cleanBoard() {
        board.getChildren().clear();
        player_name.setText(" ");
        player_color.setText(" ");
        player_id.setText(" ");
        turings.setText(" ");
        number_of_territories.setText(" ");
        Round.setText("Round:   ");
        price_to_maintenance.setText("price to maintenance: ");
        Total_fire_power.setText("Total Fire Power ");
        territory_id.setText("Territory id: ");
        tableData.getItems().clear();
        armyTablePerTerritory.getItems().clear();



    }


    @FXML
    void ChangeStyle_onAction(ActionEvent event) {
        String style = selectStyleChoiceBox.getValue();
        String anim=selectAnimationCoiceBox.getValue();
        if (anim.equals("Animation"))
            animation1 = 1;
        if (anim.equals("Without"))
            animation1 = 0;

        if (style.equals("Default")) {
            ToolBar.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            bigBorderPane.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            armyDetail.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            mapBorder.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            board.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            fullBoard.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            data_anchor.setStyle("-fx-background-image: url('/Resources/DefaultBackground.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");

            playerColorLabel.setFont(new Font("System Bold", 11));
            PlayerIdLabel.setFont(new Font("System Bold", 11));
            PlayerNameLabel.setFont(new Font("System Bold", 11));
            TuringsLabel.setFont(new Font("System Bold", 11));
            NumberofTerritoriesLabel.setFont(new Font("System Bold", 11));
            Round.setFont(new Font("System Bold", 11));
            territory_id.setFont(new Font("System Bold", 15));

            maintenance.setFont(new Font("System Bold", 12));
            maintenance.setStyle("-fx-text-fill: #1210b3");
            calculated_risk.setFont(new Font("System Bold", 12));
            calculated_risk.setStyle("-fx-text-fill: #1210b3");
            well_orchestrated.setFont(new Font("System Bold", 12));
            well_orchestrated.setStyle("-fx-text-fill: #1210b3");
            add_army.setFont(new Font("System Bold", 12));
            add_army.setStyle("-fx-text-fill: #1210b3");
            Natural_Territory.setFont(new Font("System Bold", 12));
            Natural_Territory.setStyle("-fx-text-fill: #1210b3");
            retire.setFont(new Font("System Bold", 12));
            retire.setStyle("-fx-text-fill: #1210b3");
            confirm.setFont(new Font("System Bold", 18));
            confirm.setStyle("-fx-text-fill: #1210b3");
            skip_turn.setFont(new Font("System Bold", 12));
            skip_turn.setStyle("-fx-text-fill: #1210b3");
            endGame.setFont(new Font("System Bold", 25));
            endGame.setStyle("-fx-text-fill: #1210b3");


        }
        if (style.equals("Option 2")) {
            ToolBar.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            bigBorderPane.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            armyDetail.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            mapBorder.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            board.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            fullBoard.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            data_anchor.setStyle("-fx-background-image: url('/Resources/option2.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
            playerColorLabel.setFont(new Font("System Bold Italic", 10));
            PlayerIdLabel.setFont(new Font("System Bold Italic", 10));
            PlayerNameLabel.setFont(new Font("System Bold Italic", 10));
            TuringsLabel.setFont(new Font("System Bold Italic", 10));
            NumberofTerritoriesLabel.setFont(new Font("System Bold Italic", 10));
            Round.setFont(new Font("System Bold Italic", 10));
            territory_id.setFont(new Font("System Bold Italic", 15));

            maintenance.setFont(new Font("System Bold Italic", 12));
            maintenance.setStyle("-fx-text-fill: #d20808");
            calculated_risk.setFont(new Font("System Bold Italic", 12));
            calculated_risk.setStyle("-fx-text-fill: #d20808");
            well_orchestrated.setFont(new Font("System Bold Italic", 12));
            well_orchestrated.setStyle("-fx-text-fill: #d20808");
            add_army.setFont(new Font("System Bold Italic", 12));
            add_army.setStyle("-fx-text-fill: #d20808");
            Natural_Territory.setFont(new Font("System Bold Italic", 12));
            Natural_Territory.setStyle("-fx-text-fill: #d20808");
            retire.setFont(new Font("System Bold Italic", 12));
            retire.setStyle("-fx-text-fill: #d20808");
            confirm.setFont(new Font("System Bold Italic", 18));
            confirm.setStyle("-fx-text-fill: #d20808");
            skip_turn.setFont(new Font("System Bold Italic", 12));
            skip_turn.setStyle("-fx-text-fill: #d20808");
            endGame.setFont(new Font("System Bold Italic", 25));
            endGame.setStyle("-fx-text-fill: #d20808");
        }
        if (style.equals("Option 3")) {
            {
                ToolBar.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                bigBorderPane.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                armyDetail.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                mapBorder.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                board.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                fullBoard.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                data_anchor.setStyle("-fx-background-image: url('/Resources/option3.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
                playerColorLabel.setFont(new Font("System Italic", 11));
                PlayerIdLabel.setFont(new Font("System  Italic", 11));
                PlayerNameLabel.setFont(new Font("System  Italic", 11));
                TuringsLabel.setFont(new Font("System  Italic", 11));
                NumberofTerritoriesLabel.setFont(new Font("System  Italic", 11));
                Round.setFont(new Font("System  Italic", 11));
                territory_id.setFont(new Font("System  Italic", 20));


                maintenance.setFont(new Font("System  Italic", 12));
                maintenance.setStyle("-fx-text-fill: #03470c");
                calculated_risk.setFont(new Font("System Italic", 12));
                calculated_risk.setStyle("-fx-text-fill: #03470c");
                well_orchestrated.setFont(new Font("System Italic", 12));
                well_orchestrated.setStyle("-fx-text-fill: #03470c");
                add_army.setFont(new Font("System Italic", 12));
                add_army.setStyle("-fx-text-fill: #03470c");
                Natural_Territory.setFont(new Font("System Italic", 12));
                Natural_Territory.setStyle("-fx-text-fill: #03470c");
                retire.setFont(new Font("System Italic", 12));
                retire.setStyle("-fx-text-fill: #03470c");
                confirm.setFont(new Font("System Italic", 18));
                confirm.setStyle("-fx-text-fill: #03470c");
                skip_turn.setFont(new Font("System Italic", 12));
                skip_turn.setStyle("-fx-text-fill: #03470c");
                endGame.setFont(new Font("System Italic", 24));
                endGame.setStyle("-fx-text-fill: #03470c");
            }
        }


        FXMLLoader load= new FXMLLoader();
        load.setLocation(getClass().getResource("war.fxml"));
        Parent root= null;
        try {
            root = load.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage=new Stage();

        stage.setScene(new Scene(root,300,300));
        stage.show();


    }

    @FXML
    void loadGame_onAction(ActionEvent event) throws IOException {
        startGame.setDisable(true);
        cleanBoard();

        loadTheGame();
        checkTerritoriesColor();


    }

    private void checkTerritoriesColor() {

        board.getChildren().stream().forEach(e-> setBoxStyle((VBox) e,(board.getRowIndex(e) - 1) * engine.getDescriptor().getColumns() + board.getColumnIndex(e)));

    }

    @FXML
    void Maintaince_OnAction(ActionEvent event) {
        // tableData.setDisable(false);
        confirm.setDisable(false);
        option = 1;
    }

    @FXML
    void calculatedRisk_OnAction(ActionEvent event) {
        tableData.setDisable(false);
        confirm.setDisable(false);
        option = 2;
    }

    @FXML
    void wellOrchestrated_OnAction(ActionEvent event) {
        tableData.setDisable(false);
        confirm.setDisable(false);
        option = 3;
    }

    @FXML
    void naturalTerritory_OnAction(ActionEvent event) {
        tableData.setDisable(false);
        confirm.setDisable(false);
        option = 4;
    }

    @FXML
    void AddArmy_OnAction(ActionEvent event) {
        tableData.setDisable(false);
        confirm.setDisable(false);
        option = 5;
    }

    @FXML
    void retire_onAction(ActionEvent event) {
        option = 6;
        confirm.setDisable(false);

    }

    @FXML
    void Confirm_OnAction(ActionEvent event) {
        Battle battle=new Battle();
        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(territoryId);
        if (option == 1)//1=maintaince
        {
            Supplier<Integer> enoughMoney = () -> GameEngine.gameManager.getRehabilitationArmyPriceInTerritory(targetTerritory);
            if (engine.getGameManager().isSelectedPlayerHasEnoughMoney(enoughMoney)) {
                int price = enoughMoney.get();
                engine.getGameManager().rehabilitateSelectedTerritoryArmy();
                engine.getGameManager().getCurrentPlayerTurn().decrementFunds((price));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("You Dont have enough Money \n Skip Turn");
                alert.show();

            }

        }

        if (option == 2)//2=calculatedRisk
        {


            if (canBuildArmyFromData()) {
                Army PreattackerArmy = buildArmyFromData();
                Army PredefenceArmy = engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce();

                battle.preparedToBattle(PredefenceArmy, PreattackerArmy, engine.getDescriptor().getTerritoryMap().get(territoryId));
                showWarStatus(PreattackerArmy, PredefenceArmy);

                if (battle.isAttackSucceed()) {

                    battle.updateArmiesAfterAttackerVictory();
                    engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().deleteTerritory(territoryId);
                    engine.getDescriptor().getTerritoryMap().get(territoryId).setConquer(engine.getGameManager().getCurrentPlayerTurn());

                    engine.getGameManager().getCurrentPlayerTurn().addTerritory(engine.getDescriptor().getTerritoryMap().get(territoryId));
                    //print that attacker win.



                } else {

                    battle.updateArmiesAfterAttackerDefeat();
                    //print that attacker lose



                }
                winner_war.setText("The Battle Winner: "+engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().getPlayer_name());


            }
            if (battle.isWinnerArmyNotStrongEnoughToHoldTerritory()) {
                engine.getDescriptor().getTerritoryMap().get(territoryId).xChangeFundsForUnitsAndHold();
            }


        }
        if (option == 3)// 3=well-orchestrated
        {
            warZone.setVisible(true);

            if (canBuildArmyFromData()) {
                Army attackerArmy = buildArmyFromData();
                battle.preparedToBattle(engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce(), attackerArmy, engine.getDescriptor().getTerritoryMap().get(territoryId));
                showWarStatus(attackerArmy,engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce());
                if (battle.iswellOrchestratedAttackSucceed(engine.getDescriptor().getUnitMap().size()) == 1)//attacker win
                {
                    battle.updateArmiesAfterAttackerwellOrchestratedVictory(engine.getDescriptor().getUnitMap().size());
                    engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().deleteTerritory(territoryId);
                    engine.getDescriptor().getTerritoryMap().get(territoryId).setConquer(engine.getGameManager().getCurrentPlayerTurn());
                    engine.getGameManager().getCurrentPlayerTurn().addTerritory(engine.getDescriptor().getTerritoryMap().get(territoryId));
                } else {
                    battle.updateArmiesAfterAttackerwellOrchestratedDefeat(engine.getDescriptor().getUnitMap().size());
                    attackerArmy = null;
                }
                winner_war.setText("The Battle Winner: "+engine.getDescriptor().getTerritoryMap().get(territoryId).getConquer().getPlayer_name());


            }
            if (battle.isWinnerArmyNotStrongEnoughToHoldTerritory())
                engine.getDescriptor().getTerritoryMap().get(territoryId).xChangeFundsForUnitsAndHold();
        }
        if (option == 4)//,4=natural Territory
        {
            if (canBuildArmyFromData()) {
                Army army = buildArmyFromData();
                engine.getGameManager().setSelectedArmyForce(army);
                if (engine.getGameManager().conquerNeutralTerritory()) {

                    int price = pricetobuy();
                    engine.getGameManager().getCurrentPlayerTurn().decrementFunds(price);
                    skip_turn.setDisable(false);
                }
            }

        }
        if (option == 5)//5=add army
        {
            int Amount;
            if (canBuildArmyFromData()) {
                List<String> l = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());

                for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {
                    String unitType = l.get(i);
                    Person person = (Person) tableData.getItems().get(i);
                    if (!person.getAmount().getText().isEmpty()) {

                        Amount = Integer.parseInt(person.getAmount().getText());
                    }
                    else
                       Amount=0;

                    for (int j = 0; j < Amount; j++) {

                        int rank = engine.getDescriptor().getUnitMap().get(unitType).getRank();
                        int purchase = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
                        int Maxfp = engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower();
                        int competenceReduction = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();

                        Unit unit = new Unit(unitType, rank, purchase, Maxfp, competenceReduction);
                        engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce().addUnit(unit);
                    }

                }
                int price = pricetobuy();
                engine.getGameManager().getCurrentPlayerTurn().decrementFunds(price);
            }
        }
        if (option == 6) {
            engine.deletePlayer();
            checkTerritoriesColor();
            if (engine.getDescriptor().getPlayersList().size() == 1)
            {
                warZone.setVisible(true);
                winner_war.setText("The Winner of the game: " + engine.getDescriptor().getPlayersList().get(0).getPlayer_name());
                engine.getGameManager().setCurrentPlayerTurn(engine.getDescriptor().getPlayersList().get(0));
                load_game.setDisable(false);
                LoadXML.setDisable(false);
                saveGame.setDisable(true);

            }
            retire.setDisable(true);

    }
        setDataright();
        drawArmyTableData(territoryId);
        tableData.setDisable(true);
        confirm.setDisable(true);
        turnOffButtons();
        setBoxStyle(currentTerritoryBox,territoryId);
        if(engine.getDescriptor().getPlayersList().size() == 1)
        {
            warZone.setVisible(true);
            winner_war.setText("The Winner of the game: " + engine.getDescriptor().getPlayersList().get(0).getPlayer_name());
            winner();
            skip_turn.setDisable(true);

        }
        else {
            skip_turn.setDisable(false);
        }
        board.setDisable(true);

    }

    private void showWarStatus(Army PreattackerArmy, Army PredefenceArmy) { //noam
        final ObservableList<popUpdata> attackdata = FXCollections.observableArrayList();
        final ObservableList<popUpdata> defenceData = FXCollections.observableArrayList();

        warZone.setVisible(true);

        A_unit_war.setCellValueFactory(new PropertyValueFactory<popUpdata, String>("unit"));
        D_unit_war.setCellValueFactory(new PropertyValueFactory<popUpdata, String>("unit"));

        A_fp_war.setCellValueFactory(new PropertyValueFactory<popUpdata, Integer>("fp"));
        D_fp_war.setCellValueFactory(new PropertyValueFactory<popUpdata, Integer>("fp"));

        A_hm_war.setCellValueFactory(new PropertyValueFactory<popUpdata, Integer>("amount"));
        D_hm_war.setCellValueFactory(new PropertyValueFactory<popUpdata, Integer>("amount"));

        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());




        for (int i = 0; i < unitMapp.size(); i++) {

            String unitType = unitMapp.get(i);
            int preAttackAmount = PreattackerArmy.howMachFromThisUnitType(unitType);
            int preDefenceAmount = PredefenceArmy.howMachFromThisUnitType(unitType);
            int preAttackFp = PreattackerArmy.getTotalPowerPerUnit(unitType);
            int preDefenceFp = PredefenceArmy.getTotalPowerPerUnit(unitType);
            attackdata.add(new popUpdata(unitType, preAttackAmount, preAttackFp));
            defenceData.add(new popUpdata(unitType, preDefenceAmount, preDefenceFp));

        }

        A_table_war.setEditable(true);
        D_table_war.setEditable(true);
        A_table_war.setItems(attackdata);
        D_table_war.setItems(defenceData);



    }

    private void turnOffButtons() {

        maintenance.setDisable(true);
        calculated_risk.setDisable(true);
        well_orchestrated.setDisable(true);
        Natural_Territory.setDisable(true);
        add_army.setDisable(true);
        tableData.setDisable(true);
        confirm.setDisable(true);

    }

    @FXML
    void SkipTurn_onAction(ActionEvent event) {
        ObservableList<Person> data = FXCollections.observableArrayList();

        data.add( new Person(" ", 0, "", 0, 0, 0));

        tableData.setItems(data);
        drawTableData();


        warZone.setVisible(false);

        board.setDisable(false);

        option = 0;

        if (engine.getGameManager().isCycleOver()) {
            engine.getGameManager().startOfRoundUpdates();
            engine.getGameManager().endOfRoundUpdates();
            engine.getGameManager().nextPlayerInTurn();
            setDataright();
            checkTerritoriesColor();
        } else {

            GameEngine.gameManager.nextPlayerInTurn();
            setDataright();
        }
        if (GameEngine.gameManager.isGameOver())
        {
            if (getWinnerPlayer()!=null )
            {
                warZone.setVisible(true);
                winner_war.setText("The Winner of the game: " + getWinnerPlayer().getPlayer_name());
                winner();
            }
            else
                {
                warZone.setVisible(true);
                    winner_war.setText("            TIE!");
                    winner_war.setFont(new Font("System Bold", 28));
                    winner_war.setStyle("-fx-text-fill: #b210a3");
            }

                Round.setText("Round: Game Over!");
                load_game.setDisable(false);
                LoadXML.setDisable(false);
                saveGame.setDisable(true);
                skip_turn.setDisable(true);
                retire.setDisable(true);
                warZone.setVisible(true);
                turnOffButtons();
            }



        if (engine.getGameManager().roundNumber == 1) {
            retire.setDisable(true);
            skip_turn.setDisable(true);
        }

        armyTablePerTerritory.setVisible(false);
        Total_fire_power.setText("Total fire power: ");
        price_to_maintenance.setText("Price to maintenance: ");



    }

    @FXML
    void saveGame_onAction(ActionEvent event) throws IOException {
        try {
            saveTheGame();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("THERE WAS AN ERROR SAVING THE GAME");
            alert.show();

        }
    }


    @FXML
    void loadXml_onAction(ActionEvent event) {
        startGame.setDisable(true);
        cleanBoard();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        //send message if the xml is not valid
        if (file != null) {

            try {
                try {
                    engine.loadXML(file.getPath());
                    startGame.setDisable(false);
                    engine.newGame();
                    drawBoard();
                } catch (GameDescriptor.dubbPlayernameException e) {


                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Error in the Players details of the XML names \n There are at least one name that showing twice");
                    alert.show();
                } catch (GameDescriptor.noDefaultProfitException e) {


                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("No default profit in XML");
                    alert.show();

                } catch (GameDescriptor.IllegalunitdetailsorNamedubbException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Error in the unit details of the XML \n ID or Name is showing more than 1");
                    alert.show();

                } catch (GameDescriptor.colandRowException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("THERE WAS AN ERROR Loading THE XML \n number of columns or rows is not valid");
                    alert.show();

                } catch (GameDescriptor.dubbIDException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("THERE WAS AN ERROR Loading THE XML \n Double ID in xml detected");
                    alert.show();

                } catch (GameDescriptor.dubbPlayerIdException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Error in the Players details of the XML \n There are at least one ID that showing twice");
                    alert.show();

                } catch (GameDescriptor.IllegalDupprankortypeException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Error in the unit details of the XML \n There are duplicate of Rank or Type to the units");
                    alert.show();

                } catch (GameDescriptor.IllegalunitdetailsorderException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Error in the unit details of the XML \n Id's order are not right");
                    alert.show();

                } catch (GameDescriptor.noDefaultThresholdException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("No default Army Threshold in XML");
                    alert.show();
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("THERE WAS AN ERROR Loading THE XML");
                alert.show();
            }catch (IllegalArgumentException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("THERE WAS AN ERROR \n One of the XML details is not valid");
                alert.show();
            }
        }


    }

    @FXML
    void startGame_onAction(ActionEvent event) {
        if (selectStyleChoiceBox.getItems().isEmpty()) {

            selectStyleChoiceBox.getItems().add("Default");
            selectStyleChoiceBox.getItems().add("Option 2");
            selectStyleChoiceBox.getItems().add("Option 3");
            selectStyleChoiceBox.setValue("Default");
            selectAnimationCoiceBox.getItems().add("Animation");
            selectAnimationCoiceBox.getItems().add("Without");
            selectAnimationCoiceBox.setValue("Animation");
        }

        gameStarted = true;
        endGame.setDisable(false);
        startGame.setDisable(true);
        load_game.setDisable(true);
        LoadXML.setDisable(true);
        saveGame.setDisable(false);

        drawTableData();
        engine.getGameManager().nextPlayerInTurn();
        setDataright();
        board.setDisable(false);

    }

    public int pricetobuy() {
        int sumOfprice = 0;
        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {
            Person person = (Person) tableData.getItems().get(i);
            if (!person.getAmount().getText().isEmpty()) {
                int Amount = Integer.parseInt(person.getAmount().getText());
                int price = person.getPrice();
                sumOfprice = sumOfprice + Amount * price;
            }
        }
        return sumOfprice;
    }

    public boolean canBuildArmyFromData() {
        int sumOfprice = 0;
        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {

            Person person = (Person) tableData.getItems().get(i);
            if (!person.getAmount().getText().isEmpty()) {
                int Amount = Integer.parseInt(person.getAmount().getText());
                int price = Integer.parseInt(person.getAmount().getText());
                sumOfprice = sumOfprice + Amount * price;
            }
        }
        if (sumOfprice <= engine.getGameManager().getCurrentPlayerTurn().getFunds())
            return true;
        else
            return false;
    }

    public Army buildArmyFromData() {
        Army army = new Army();
        List<String> l = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());

        for (int i = 0; i < engine.getDescriptor().getUnitMap().size(); i++) {

            String unitType = l.get(i);
            Person person = (Person) tableData.getItems().get(i);
            if (!person.getAmount().getText().isEmpty()) {
                int Amount = Integer.parseInt(person.getAmount().getText());
                for (int j = 0; j < Amount; j++) {

                    int rank = engine.getDescriptor().getUnitMap().get(unitType).getRank();
                    int purchase = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
                    int Maxfp = engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower();
                    int competenceReduction = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();

                    Unit unit = new Unit(unitType, rank, purchase, Maxfp, competenceReduction);
                    army.addUnit(unit);
                }
            }
        }
        return army;
    }

    public void drawTableData() {


        final ObservableList<Person> data = FXCollections.observableArrayList();
        tableData.setEditable(true);
        amount.setCellValueFactory(new PropertyValueFactory<Person, TextField>("amount"));
        price.setCellValueFactory(new PropertyValueFactory<Person, Integer>("price"));
        type.setCellValueFactory(new PropertyValueFactory<Person, String>("type"));
        fp.setCellValueFactory(new PropertyValueFactory<Person, Integer>("fp"));
        Subduction.setCellValueFactory(new PropertyValueFactory<Person, Integer>("Subduction"));
        rank.setCellValueFactory(new PropertyValueFactory<Person, Integer>("Rank"));

        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());

        for (int i = 0; i < unitMapp.size(); i++) {

            String unitType = unitMapp.get(i);
            int price1 = engine.getDescriptor().getUnitMap().get(unitType).getPurchase();
            int subduction1 = engine.getDescriptor().getUnitMap().get(unitType).getCompetenceReduction();
            int rank1 = engine.getDescriptor().getUnitMap().get(unitType).getRank();
            data.add(new Person(unitType, engine.getDescriptor().getUnitMap().get(unitType).getMaxFirePower(), "", price1, subduction1, rank1));
        }
        tableData.setMaxHeight(200);
        tableData.setItems(data);




    }

    public static class Person {
        private TextField amount;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty price;
        private final SimpleIntegerProperty fp;
        private final SimpleIntegerProperty subduction;
        private final SimpleIntegerProperty rank;

        private Person(String type, int fp, String amount, int price, int subduction, int rank) {
            this.amount = new TextField(amount);
            this.type = new SimpleStringProperty(type);
            this.fp = new SimpleIntegerProperty(fp);
            this.price = new SimpleIntegerProperty(price);
            this.subduction = new SimpleIntegerProperty(subduction);
            this.rank = new SimpleIntegerProperty(rank);
        }

        public SimpleIntegerProperty priceProperty() {
            return price;
        }

        public int getSubduction() {
            return subduction.get();
        }

        public int getRank() {
            return rank.get();
        }


        public SimpleIntegerProperty subductionProperty() {
            return subduction;
        }

        public SimpleIntegerProperty rankProperty() {
            return rank;
        }

        public void setSubduction(int subduction) {
            this.subduction.set(subduction);
        }

        public void setRank(int rank) {
            this.rank.set(rank);
        }

        public TextField getAmount() {
            return amount;
        }

        public void setAmount(TextField amount) {
            this.amount = amount;
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public int getFp() {
            return fp.get();
        }

        public SimpleIntegerProperty fpProperty() {
            return fp;
        }

        public int getPrice() {
            return price.get();
        }

        public void setPrice(int price) {
            this.price.set(price);
        }

        public void setFp(int fp) {
            this.fp.set(fp);
        }
    }
    public static class popUpdata {


        private SimpleStringProperty  unit;
        private SimpleIntegerProperty amount;
        private SimpleIntegerProperty fp;


        popUpdata(String unit,int amout,int fp){

            this.unit=new SimpleStringProperty(unit);
            this.amount=new SimpleIntegerProperty(amout);
            this.fp =new SimpleIntegerProperty(fp);


        }

        public String getUnit() {
            return unit.get();
        }

        public SimpleStringProperty unitProperty() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit.set(unit);
        }

        public int getAmount() {
            return amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount.set(amount);
        }

        public int getFp() {
            return fp.get();
        }

        public SimpleIntegerProperty fpProperty() {
            return fp;
        }

        public void setFp(int fp) {
            this.fp.set(fp);
        }
    }

    public void drawArmyTableData(int territoryID) {


        final ObservableList<Person1> data = FXCollections.observableArrayList();
        territory_id.setText("Territory Id: " + territoryID);
        armyTablePerTerritory.setEditable(true);


        amountPerTerritory.setCellValueFactory(new PropertyValueFactory<Person1, Integer>("amount"));
        maintenancePerTerritory.setCellValueFactory(new PropertyValueFactory<Person1, Integer>("maintenance"));
        RankArmy.setCellValueFactory(new PropertyValueFactory<Person1, Integer>("Rank"));
        typePerTerritory.setCellValueFactory(new PropertyValueFactory<Person1, String>("type"));
        fpPerTerritory.setCellValueFactory(new PropertyValueFactory<Person1, Integer>("fp"));

        List<String> unitMapp = new ArrayList<String>(engine.getDescriptor().getUnitMap().keySet());

        for (int i = 0; i < unitMapp.size(); i++) {

            String unitType = unitMapp.get(i);
            int maintenance1, amount1, fp1, rank1;
            maintenance1 = amount1 = fp1 = rank1 = 0;
            if (engine.getGameManager().getCurrentPlayerTurn() == engine.getDescriptor().getTerritoryMap().get(territoryID).getConquer()) {
                maintenance1 = engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPriceperUnit(unitType);
                amount1 = engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().howMachFromThisUnitType(unitType);
                fp1 = engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().getTotalPowerPerUnit(unitType);
                rank1 = engine.getDescriptor().getUnitMap().get(unitType).getRank();
            }

            data.add(new Person1(unitType, fp1, amount1, maintenance1, rank1));
        }
        if (engine.getGameManager().isConquered()) {
            price_to_maintenance.setText("Price To Maintenance " + engine.getDescriptor().getTerritoryMap().get(territoryID).getConquerArmyForce().calculateRehabilitationPrice());
            Total_fire_power.setText("Total Fire Power " + engine.getDescriptor().getTerritoryMap().get(territoryId).getConquerArmyForce().getTotalPower());
        }

        armyTablePerTerritory.setMaxHeight(200);
        armyTablePerTerritory.setItems(data);


    }

    public static class Person1 {
        private final SimpleIntegerProperty maintenance;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty amount;
        private final SimpleIntegerProperty fp;
        private final SimpleIntegerProperty rank;


        private Person1(String type, int fp, int amount, int maintenance, int rank) {
            this.maintenance = new SimpleIntegerProperty(maintenance);
            this.type = new SimpleStringProperty(type);
            this.fp = new SimpleIntegerProperty(fp);
            this.amount = new SimpleIntegerProperty(amount);
            this.rank = new SimpleIntegerProperty(rank);
        }

        public int getMaintenance() {
            return maintenance.get();
        }

        public int getRank() {
            return rank.get();
        }

        public SimpleIntegerProperty maintenanceProperty() {
            return maintenance;
        }

        public void setMaintenance(int maintenance) {
            this.maintenance.set(maintenance);
        }

        public void setRank(int rank1) {
            this.rank.set(rank1);
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public int getAmount() {
            return amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public SimpleIntegerProperty rankProperty() {
            return rank;
        }

        public void setAmount(int amount) {
            this.amount.set(amount);
        }

        public int getFp() {
            return fp.get();
        }

        public SimpleIntegerProperty fpProperty() {
            return fp;
        }

        public void setFp(int fp) {
            this.fp.set(fp);
        }
    }

    public void drawBoard() {

        String neutral = "-fx-border-color: black;\n" +
                //"-fx-background-color: white; \n"+
                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";


        board.setPadding(new Insets(50));

        int columns = engine.getDescriptor().getColumns();
        int rows = engine.getDescriptor().getRows();
        int counter = 1;


        for (int r = 1; r < rows + 1; r++) {
            for (int c = 1; c < columns + 1; c++) {

                Label Id = new Label("ID: " + counter++);
                Label Yield = new Label("Yield: " + engine.getDescriptor().getTerritoryMap().get(counter - 1).getProfit());
                Label ThreshHold = new Label("Threshold: " + engine.getDescriptor().getTerritoryMap().get(counter - 1).getArmyThreshold());
                VBox territoryBox = new VBox(Id, Yield, ThreshHold);
                territoryBox.setStyle(neutral);
                    territoryBox.setOnMouseClicked((e) ->
                    {
                        armyTablePerTerritory.setVisible(true);
                        //  tableData.setDisable(false);
                        if (engine.getDescriptor().getPlayersList().size()!=0) {

                            currentTerritoryBox = territoryBox;
                            int currentRow = board.getRowIndex(territoryBox);
                            int currentCol = board.getColumnIndex(territoryBox);
                            int id = (currentRow - 1) * columns + currentCol;
                            this.territoryId = id;
                            engine.getGameManager().setSelectedTerritoryByPlayer(engine.getDescriptor().getTerritoryMap().get(id));
//                            if (engine.getGameManager().isConquered()) {
//                                Natural_Territory.setDisable(true);
//                            }
                            drawArmyTableData(id);
                            whatToNotDisable(id);
                        }


                    });




                Id.setTextFill(Paint.valueOf("crimson"));
                Id.setFont(Font.font(11));
                Yield.setFont(Font.font(10));
                ThreshHold.setFont(Font.font(10));
                territoryBox.setPrefSize(100, 80);
                territoryBox.setMinSize(100, 80);
                territoryBox.setMaxSize(100, 80);

                board.add(territoryBox, c, r);
            }
        }

        board.autosize();
        board.setGridLinesVisible(false);

//
    }

    private void setBoxStyle(VBox territoryBox, int id) {

        String neutral = "-fx-border-color: black;\n" +
                //"-fx-background-color: white; \n"+
                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";

        String player1 = "-fx-border-color: red;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        String player2 = "-fx-border-color: blue;\n" +

                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        String player3 = "-fx-border-color: green;\n" +
                //"-fx-background-color: white;\n"+
                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        String player4 = "-fx-border-color: yellow;\n" +
                // "-fx-background-color: white;\n"+
                "-fx-border-insets: 5;\n" +
                "-fx-padding: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        if (engine.getDescriptor().getTerritoryMap().get(id).isConquered()) {
            Natural_Territory.setDisable(true);
            switch (engine.getDescriptor().getTerritoryMap().get(id).getConquer().getColor()) {
                case 1: {
                    if (animation1 == 1) {
                        final Animation animation = new Transition() {

                            {
                                setCycleDuration(Duration.millis(1000));
                                setInterpolator(Interpolator.EASE_OUT);
                            }

                            @Override
                            protected void interpolate(double frac) {
                                Color vColor = new Color(1, 0, 0, 1 - frac);
                                territoryBox.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        };
                        animation.play();
                    }
                    territoryBox.setStyle(player1);
                    break;
                }
                case 2: {
                    if (animation1 == 1) {
                        final Animation animation = new Transition() {

                            {
                                setCycleDuration(Duration.millis(1000));
                                setInterpolator(Interpolator.EASE_OUT);
                            }

                            @Override
                            protected void interpolate(double frac) {
                                Color vColor = new Color(0, 0, 1, 1 - frac);
                                territoryBox.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        };
                        animation.play();

                    }
                    territoryBox.setStyle(player2);
                    break;
                }
                case 3: {
                    if (animation1 == 1) {
                        final Animation animation = new Transition() {

                            {
                                setCycleDuration(Duration.millis(1000));
                                setInterpolator(Interpolator.EASE_OUT);
                            }

                            @Override
                            protected void interpolate(double frac) {
                                Color vColor = new Color(0, 1, 0, 1 - frac);
                                territoryBox.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        };
                        animation.play();

                    }
                    territoryBox.setStyle(player3);
                    break;
                }
                case 4: {
                    if (animation1 == 1) {
                        final Animation animation = new Transition() {

                            {
                                setCycleDuration(Duration.millis(1000));
                                setInterpolator(Interpolator.EASE_OUT);
                            }

                            @Override
                            protected void interpolate(double frac) {
                                Color vColor = new Color(1, 1, 0, 1 - frac);
                                territoryBox.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        };
                        animation.play();

                    }
                    territoryBox.setStyle(player4);
                    break;
                }


            }
        } else {

            if (animation1 == 1) {
                final Animation animation = new Transition() {

                    {
                        setCycleDuration(Duration.millis(1000));
                        setInterpolator(Interpolator.EASE_OUT);
                    }

                    @Override
                    protected void interpolate(double frac) {
                        Color vColor = new Color(0, 0, 0, 1 - frac);
                        territoryBox.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                };
                animation.play();

            }
            territoryBox.setStyle(neutral);
        }


    }

    public Player getWinnerPlayer() {
        int winnerPlayerID = 1, maxScore = 0;
        int winnerPlayerOrder = 5;
        int size = engine.getDescriptor().getPlayersList().size();
        int[] playerScores = new int[size];

        for (int i = 0; i < size; i++) playerScores[i] = 0;
        int count = 0;
        for (Player player : engine.getDescriptor().getPlayersList()) {

            for (Integer territoryID : player.getTerritoriesID()) {
                playerScores[count] += engine.getDescriptor().getTerritoryMap().get(territoryID).getProfit();
            }
            count++;
        }

        for (int i = 0; i < size; i++) {

            if (playerScores[i] >= maxScore) {
                maxScore = playerScores[i];
                winnerPlayerID = engine.getDescriptor().getPlayersList().get(i).getID();
                winnerPlayerOrder = i;
            }
        }

        for (int i = 0; i < size; i++) {
            if (playerScores[i] == maxScore && winnerPlayerID != engine.getDescriptor().getPlayersList().get(i).getID())
                return null;
        }
        return engine.getDescriptor().getPlayersList().get(winnerPlayerOrder);
    }

    private void winner() {
        Player winner = getWinnerPlayer();
        i = 1;
        int id = 0;
        VBox territory1;
        while (winner.getTerritoriesID().size() >= i) {
            id = winner.getTerritoriesID().get(i-1).intValue();
            if (animation1 == 1) {
                territory1 = (VBox) board.getChildren().get(id-1);
                VBox finalTerritory = territory1;
                final Animation animation = new Transition() {
                    {
                        setCycleDuration(Duration.millis(5000));
                        setInterpolator(Interpolator.EASE_OUT);
                    }

                    @Override
                    protected void interpolate(double frac) {
                        Color vColor = new Color(1,1,0,1 - frac);
                        finalTerritory.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                };
                animation.play();

            }
            i++;
        }
    }



    private void whatToNotDisable(int id) {

        Territory targetTerritory = engine.getDescriptor().getTerritoryMap().get(id);
        GameEngine.gameManager.setSelectedTerritoryForTurn(targetTerritory);
        if (gameStarted) {
            if (engine.getGameManager().roundNumber == 1) {

                retire.setDisable(true);
                skip_turn.setDisable(true);
                if (!GameEngine.gameManager.isConquered()) {

                    Natural_Territory.setDisable(false);
                    well_orchestrated.setDisable(true);
                    calculated_risk.setDisable(true);
                    maintenance.setDisable(true);
                    add_army.setDisable(true);



                } else {
                    turnOffButtons();
                }

            } else {
                if (engine.getGameManager().isGameOver() || engine.getDescriptor().getPlayersList().size() == 1) {
                    retire.setDisable(true);
                    skip_turn.setDisable(true);
                    turnOffButtons();


                } else {
                    retire.setDisable(false);
                    skip_turn.setDisable(false);
                    if (GameEngine.gameManager.isConquered()) {
                        if (GameEngine.gameManager.isTerritoryBelongsCurrentPlayer()) {
                            maintenance.setDisable(false);
                            add_army.setDisable(false);
                            Natural_Territory.setDisable(true);
                            well_orchestrated.setDisable(true);
                            calculated_risk.setDisable(true);
                            drawArmyTableData(id);
                        } else {
                            price_to_maintenance.setText("Price to maintenance: ");
                            Total_fire_power.setText("Total fire power: ");
                            if (GameEngine.gameManager.isTargetTerritoryValid() || GameEngine.gameManager.getCurrentPlayerTurn().getTerritoriesID().size() == 0) {
                                Natural_Territory.setDisable(true);
                                well_orchestrated.setDisable(false);
                                calculated_risk.setDisable(false);
                                maintenance.setDisable(true);
                                add_army.setDisable(true);
                            } else {
                                turnOffButtons();
                            }


                        }

                    } else {
                        if (engine.getGameManager().getCurrentPlayerTurn().getTerritoriesID().size() == 0) {

                            Natural_Territory.setDisable(false);
                            well_orchestrated.setDisable(true);
                            calculated_risk.setDisable(true);
                            maintenance.setDisable(true);
                            add_army.setDisable(true);


                        } else {

                            if (GameEngine.gameManager.isTargetTerritoryValid()) {
                                Natural_Territory.setDisable(false);
                                well_orchestrated.setDisable(true);
                                calculated_risk.setDisable(true);
                                maintenance.setDisable(true);
                                add_army.setDisable(true);

                            } else {
                                turnOffButtons();
                            }
                        }
                    }
                }
            }
        }
    }





    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    private void setDataright() {
        Round.setText(""+engine.getGameManager().roundNumber+" / " +engine.getDescriptor().getTotalCycles());
        if (engine.getGameManager().getCurrentPlayerTurn().getColor()==1) {
            player_color.setText("RED");
            player_color.setTextFill(Color.RED);
        }
        if (engine.getGameManager().getCurrentPlayerTurn().getColor()==2) {
            player_color.setText("BLUE");
            player_color.setTextFill(Color.BLUE);
        }
        if (engine.getGameManager().getCurrentPlayerTurn().getColor()==3) {
            player_color.setText("GREEN");
            player_color.setTextFill(Color.GREEN);
        }
        if (engine.getGameManager().getCurrentPlayerTurn().getColor()==4) {
            player_color.setText("YELLOW");
            player_color.setTextFill(Color.YELLOW);
        }
       player_name.textProperty().set(engine.getGameManager().getCurrentPlayerTurn().getPlayer_name());
        player_id.setText(""+ engine.getGameManager().getCurrentPlayerTurn().getID());
        number_of_territories.setText(""+ engine.getGameManager().getCurrentPlayerTurn().getTerritoriesID().size());
        turings.setText(""+ engine.getGameManager().getCurrentPlayerTurn().getFunds());

    }



    public void saveTheGame() throws IOException {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (.txt)", ".txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());
        ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(file))
        {};
        write.writeObject(engine.getGameManager());
        write.close();

    }


    public void loadTheGame() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load File");
        File file = fileChooser.showOpenDialog(new Stage());
        //send message if the xml is not valid
        if (file != null) {
            if (engine.loadGame(file.toPath())) {
                //gameStarted = true;
                engine.setDescriptor(GameEngine.gameManager.getDescriptor());
                drawBoard();
                board.setDisable(true);
                //need to run on all territory and paint it
                startGame.setDisable(false);
            }
            else
            {
                Alert alert =new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("THERE WAS AN ERROR Loading THE GAME");
                alert.show();

            }
        }
    }

}
