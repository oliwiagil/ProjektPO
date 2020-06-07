package Checkers;

import SpaceInv.GameOverWindow;
import SpaceInv.SpaceInv;
import SpaceInv.SpaceInvMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultWindow extends Stage {


    private CheckersMenu checkersMenu = null;
    private ResultWindow resultWindow = null;

    private GridPane pane;
    private Scene scene;


    ResultWindow(CheckersMenu s){
        checkersMenu = s;
        resultWindow = this;
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

        resultWindow.initStyle(StageStyle.UNDECORATED);

        Label txt = new Label();
        txt.setText("Player " + checkersMenu.game.winnedPlayer + " has won!");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 15));

        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(15);
        pane.setVgap(5);

        Button btnAgain = new Button("Again");
        btnAgain.setOnAction((event) ->{
                applyScore(checkersMenu.game.winnedPlayer);
                checkersMenu.game.close();
                checkersMenu.startGame();
                resultWindow.close();
        });


        Button btnMenu = new Button("Menu");
        btnMenu.setOnAction(ActionEvent->{
                applyScore(checkersMenu.game.winnedPlayer);
                checkersMenu.game.gameStage.hide();
                checkersMenu.open();
                resultWindow.close();
        });

        pane.add(txt, 0, 0, 4, 1);
        pane.add(btnAgain, 0, 2);
        pane.add(btnMenu, 4, 2);
        pane.setPadding(new Insets(15));

        scene = new Scene(pane, 300, 100);
        resultWindow.setScene(scene);
        resultWindow.setTitle("Game Over");

        //This modality locks all windows except this one,
        //so first of all we must interact with this window
        resultWindow.initModality(Modality.APPLICATION_MODAL);
        resultWindow.setScene(scene);
    }

    private void applyScore(CharSequence charName){// who  when

        StringBuilder name = new StringBuilder();
        for(int i = 0; i<charName.length(); i++){
            name.append(charName.charAt(i));
        }

        File f = new File("scores/Checkers/scores.txt");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        try(FileWriter fW = new FileWriter(f, true)){
            fW.append(name.toString() + " " + checkersMenu.game.lostPlayer + " " + LocalDateTime.now().format(format)+"\n");
        } catch(Exception e){
            System.out.println("FILE: ResultWindow.java, ERROR: Cannot apply score.");
        }

    }

    private boolean isSpace(CharSequence charName){
        boolean var = false;

        for(int i = 0; i<charName.length(); i++){
            if(charName.charAt(i) == ' ') var = true;
        }

        return var;
    }


}
