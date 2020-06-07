package Snake;

import Menu.MainMenu;
import SpaceInv.SpaceInv;
import SpaceInv.SpaceInvMenu;
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

public class SnakeMenu {

    SnakeMenu snakeMenu = null;
    MainMenu mainMenu = null;
    Snake game = null;
    sSettings settings = null;


    Pane pane = null;
    Stage stage = null;
    Scene scene = null;

    final int height = 150, width = 200;

    public SnakeMenu(MainMenu mm){
        mainMenu = mm;
        snakeMenu = this;
        stage = new Stage();
        settings = new sSettings(snakeMenu);

        createMenu();
    }

    public void open(){
        stage.show();
    }

    public void close(){
        stage.hide();
    }

    public void startGame(){
        System.gc();
        try{
            if(game == null){ game = new Snake(snakeMenu); }
            game.resetGame();
            game.startGame();
        } catch (Exception e) {
            System.out.println("FILE: SnakeMenu.java");
            e.printStackTrace();
        } finally{
            snakeMenu.close();
        }
    }

    private void createMenu(){
        MyVBox box = new MyVBox();
        pane = new Pane();
        pane.getChildren().add(box);
        scene = new Scene(pane, width, height);

        stage.setScene(scene);
        stage.setTitle("Snake");
    }

    private class MyVBox extends VBox {

        final int btnWidth = 150;

        MyVBox(){
            Label lbl = new Label("Snake");
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setFont(Font.font("Serif", 20));

            //Start Button //------------------------------------------------
            Button btnStart = new Button("Start");
            btnStart.setPrefWidth(btnWidth);
            btnStart.setMinWidth(btnWidth);
            btnStart.setOnAction(ActionEvent  ->{
                startGame();
            });

            //Settings Button //---------------------------------------------
            Button btnSett = new Button("Settings");
            btnSett.setOnAction(ActionEvent->{
                settings.open();
                snakeMenu.close();
            });
            btnSett.setPrefWidth(btnWidth);
            btnSett.setMinWidth(btnWidth);

            //Scores Button //-----------------------------------------------
            Button btnScores = new Button("Scores");
            btnScores.setPrefWidth(btnWidth);
            btnScores.setMinWidth(btnWidth);

            //Back Button //-------------------------------------------------
            Button btnBack = new Button("Back");
            btnBack.setPrefWidth(btnWidth);
            btnBack.setMinWidth(btnWidth);
            btnBack.setOnAction(ActionEvent ->{
                snakeMenu.close();
                mainMenu.open();
            });

            this.setSpacing(7);
            this.setPadding(new Insets(20));
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(lbl, btnStart, btnSett,btnBack);

        }


    }


}
