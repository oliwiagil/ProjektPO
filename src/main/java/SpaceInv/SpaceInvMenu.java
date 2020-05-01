package SpaceInv;

import Menu.MainMenu;
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

public class SpaceInvMenu {

    SpaceInvMenu spaceInvMenu = null;
    MainMenu mainMenu = null;
    SpaceInv game = null;

    Pane pane = null;
    Stage stage = null;
    Scene scene = null;

    final int height = 250, width = 200;

    public SpaceInvMenu(MainMenu menu){

        spaceInvMenu = this;
        stage = new Stage();
        mainMenu = menu;
        createMenu();
        open();
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
            if(game == null){ game = new SpaceInv(spaceInvMenu); }
            game.resetGame();
            game.startGame();
        } catch (Exception e) {
            System.out.println("FILE: SpaceInvMenu.java");
            e.printStackTrace();
        } finally{
            spaceInvMenu.close();
        }
    }

    private void createMenu(){

        MyVBox box = new MyVBox();
        pane = new Pane();
        pane.getChildren().add(box);
        scene = new Scene(pane, width, height);

        stage.setScene(scene);
        stage.setTitle("Space Invaders");
    }

    private class MyVBox extends VBox {

        final int btnWidth = 150;

        MyVBox(){

            Label lbl = new Label("Space Invaders");
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setFont(Font.font("Serif", 20));

            Button btnStart = new Button("Start");
            btnStart.setPrefWidth(btnWidth);
            btnStart.setMinWidth(btnWidth);
            btnStart.setOnMouseClicked(MouseEvent ->{
                startGame();
            });

            Button btnSett = new Button("Settings");
            btnSett.setPrefWidth(btnWidth);
            btnSett.setMinWidth(btnWidth);

            Button btnScores = new Button("Scores");
            btnScores.setPrefWidth(btnWidth);
            btnScores.setMinWidth(btnWidth);

            Button btnBack = new Button("Back");
            btnBack.setPrefWidth(btnWidth);
            btnBack.setMinWidth(btnWidth);
            btnBack.setOnAction(ActionEvent ->{
                spaceInvMenu.close();
                mainMenu.open();
            });

            this.setSpacing(7);
            this.setPadding(new Insets(20));
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(lbl, btnStart, btnSett, btnScores, btnBack);
        }
    }
}