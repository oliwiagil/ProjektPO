package Checkers;

import Menu.MainMenu;
import SpaceInv.SpaceInv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CheckersMenu{

    CheckersMenu checkersMenu = null;
    MainMenu mainMenu = null;
    Checkers game = null;
    Settings settings = null;

    Stage stage;
    Pane pane;
    Scene scene;

    final int height = 200, width = 200;

    public CheckersMenu(MainMenu mM){

        mainMenu = mM;
        checkersMenu = this;

        createWindow();
    }

    public void open(){
        stage.show();
    }

    public void close(){
        stage.hide();
    }

    public void startGame(){
        game = null;
        System.gc();
        try{
            game = new Checkers(checkersMenu);
            game.resetGame();
            game.startGame();
        } catch (Exception e) {
            System.out.println("FILE: CheckersMenu.java");
            e.printStackTrace();
        } finally{
            checkersMenu.close();
        }
    }

    private void createWindow(){

        MyVBox box = new MyVBox();
        pane = new Pane();

        pane.getChildren().add(box);

        scene = new Scene(pane, width, height);

        stage = new Stage();

        stage.setScene(scene);
        stage.setTitle("Checkers Menu");
    }

    private class MyVBox extends VBox {

        final int btnWidth = 150;

        MyVBox(){

            Label lbl = new Label("Checkers");
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setFont(Font.font("Serif", 25));

            Button btnStart = new Button("Start");
            btnStart.setOnAction(ActionEvent->{
                if(settings == null) settings = new Settings(checkersMenu);
                settings.open();
                checkersMenu.close();
            });
            btnStart.setPrefWidth(btnWidth);
            btnStart.setMinWidth(btnWidth);

            Button btnScores = new Button("Scores");
            btnScores.setOnAction(ActionEvent ->{

            });
            btnScores.setPrefWidth(btnWidth);
            btnScores.setMinWidth(btnWidth);

            Button btnBack = new Button("Back");
            btnBack.setOnAction(ActionEvent->{
                mainMenu.open();
                checkersMenu.close();
            });
            btnBack.setPrefWidth(btnWidth);
            btnBack.setMinWidth(btnWidth);

            this.setPadding(new Insets(20));
            this.setSpacing(7);
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(lbl, btnStart, btnScores, btnBack);

        }
    }
}
