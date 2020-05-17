package SpaceInv;

import Menu.MainMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GamePauseWindow {

    SpaceInvMenu spaceInvMenu = null;
    SpaceInv game = null;
    GamePauseWindow gamePauseWindow = null;

    Stage stage = null;

    GamePauseWindow(SpaceInvMenu m){
        spaceInvMenu = m;
        game = m.game;
        gamePauseWindow = this;
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
        stage.setScene(new Scene(new MyGrid()));
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
                spaceInvMenu.game.gameStage.hide();
                spaceInvMenu.open();
                gamePauseWindow.close();
            });


            Button btnBack = new Button("Back to Game");
            btnBack.setOnAction(event ->{
                gamePauseWindow.close();
                game.gameStage.toFront();
                game.pause=false;
                game.timeline.play();
            });
            btnBack.setOnKeyPressed((KeyEvent)->{
                if(KeyEvent.getCode() == KeyCode.P){
                    gamePauseWindow.close();
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
