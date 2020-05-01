package SpaceInv;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameWinWindow extends Stage {

    private SpaceInvMenu spaceInvMenu = null;
    private GameWinWindow gameWinWindow = null;

    private GridPane pane;
    private Scene scene;

    GameWinWindow(SpaceInvMenu s){
        spaceInvMenu = s;
        gameWinWindow = this;
        createWindow();
    }


    public void open(){
        show();
    }

    public void close(){
        hide();
    }

    private void createWindow(){

        gameWinWindow.initStyle(StageStyle.UNDECORATED);

        Label txt = new Label();
        txt.setText("Wonderful!");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 25));

        Label txt2 = new Label("You have reached maximum score!");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 15));

        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(15);
        pane.setVgap(5);

        Button btnAgain = new Button("Again");
        btnAgain.setOnAction((event) ->{
            SpaceInv.soundWin.stop();
            spaceInvMenu.startGame();
            gameWinWindow.close();
        });


        Button btnMenu = new Button("Menu");
        btnMenu.setOnAction(ActionEvent->{

            spaceInvMenu.game.gameStage.hide();
            SpaceInv.soundWin.stop();
            spaceInvMenu.open();
            gameWinWindow.close();
        });

        pane.add(txt, 1, 0, 3, 1);
        pane.add(txt2, 0, 1, 3, 1);
        pane.add(btnAgain, 0, 2);
        pane.add(btnMenu, 3, 2);

        scene = new Scene(pane, 300, 150);
        gameWinWindow.setScene(scene);
        gameWinWindow.setTitle("Victory");

        //This modality locks all windows except this one,
        //so first of all we must interact with this window
        gameWinWindow.initModality(Modality.APPLICATION_MODAL);
        gameWinWindow.setScene(scene);
    }

}
