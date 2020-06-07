package Checkers;

import SpaceInv.SpaceInv;
import SpaceInv.SpaceInvMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PauseWindow {


    CheckersMenu checkersMenu = null;
    Checkers game = null;
    PauseWindow pauseWindow = null;

    Stage stage = null;

    PauseWindow(CheckersMenu m){
        checkersMenu = m;
        game = m.game;
        pauseWindow = this;
        stage = new Stage();

        createWindow();
    }

    public void open(){
        stage.toFront();
        stage.show();
    }

    public void close(){
        stage.hide();
    }

    private void createWindow(){
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(new PauseWindow.MyGrid()));
        stage.setTitle("Pause");
    }

    private class MyGrid extends GridPane {

        MyGrid(){

            Label txt = new Label("Game paused");
            txt.setTextAlignment(TextAlignment.CENTER);
            txt.setFont(Font.font("Serif", 20));
            txt.setAlignment(Pos.CENTER);

            Button btnExit = new Button("Back to Menu");
            btnExit.setOnAction((event)->{
                checkersMenu.game.gameStage.hide();
                checkersMenu.open();
                pauseWindow.close();
            });


            Button btnBack = new Button("Back to Game");
            btnBack.setOnAction(event ->{
                pauseWindow.close();
                game.gameStage.toFront();
                game.pause=false;
                game.timeline.play();
            });
            btnBack.setOnKeyPressed((KeyEvent)->{
                if(KeyEvent.getCode() == KeyCode.P){
                    pauseWindow.close();
                    game.gameStage.toFront();
                    game.pause=false;
                    game.timeline.play();
                }
            });


            this.add(txt, 1, 0, 1, 1);
            this.add(btnBack, 0, 2, 1, 1);
            this.add(btnExit, 2, 2, 1, 1);
            this.setHgap(10);
            this.setVgap(10);
            this.setPadding(new Insets(30));
        }

    }


}
