package JavaFxUI;



import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;


import javafx.application.Application;



import javafx.stage.Stage;



public class GameUI extends Application {


    @Override

    public void start(Stage stage) throws Exception {

        BorderPane Pane = FXMLLoader.load(getClass().getResource("Eden_Noam.fxml"));
        Scene scene = new Scene(Pane, 979, 546);
        stage.setScene(scene);
        stage.setTitle("Conquers");
        

        stage.setResizable(true);
        stage.setMinHeight(450);
        stage.setMinWidth(835);


        stage.show();
    }






    public static void main(String[] args){
        Application.launch(args);
    }




}
