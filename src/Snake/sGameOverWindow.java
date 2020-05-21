package Snake;

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

public class sGameOverWindow extends Stage {

    private SnakeMenu snakeMenu = null;
    private sGameOverWindow gameOverWindow = null;

    private GridPane pane;
    private Scene scene;


    sGameOverWindow(SnakeMenu s){
        snakeMenu = s;
        gameOverWindow = this;
        createWindow();
    }


    public void open(){
        show();
        toFront();
    }

    public void close(){
        hide();
    }


    private void createWindow(){

        gameOverWindow.initStyle(StageStyle.UNDECORATED);

        Label txt = new Label();
        txt.setText("Good job!");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 25));

        Label txt2 = new Label("You have reached " + (snakeMenu.game.waz.size-3) + " points.");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 15));

        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(15);
        pane.setVgap(5);

        Button btnAgain = new Button("Again");
        btnAgain.setOnAction((event) ->{
            snakeMenu.startGame();
            gameOverWindow.close();
        });


        Button btnMenu = new Button("Menu");
        btnMenu.setOnAction(ActionEvent->{
            snakeMenu.game.gameStage.hide();
            snakeMenu.open();
            gameOverWindow.close();
        });

        pane.add(txt, 1, 0, 3, 1);
        pane.add(txt2, 0, 1, 3, 1);
        pane.add(btnAgain, 0, 2);
        pane.add(btnMenu, 3, 2);

        scene = new Scene(pane, 300, 150);
        gameOverWindow.setScene(scene);
        gameOverWindow.setTitle("Game Over");

        //This modality locks all windows except this one,
        //so first of all we must interact with this window
        gameOverWindow.initModality(Modality.APPLICATION_MODAL);
        gameOverWindow.setScene(scene);
    }

}
