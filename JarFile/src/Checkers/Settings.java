package Checkers;

import SpaceInv.SpaceInvMenu;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class Settings {


    Settings settings = null;
    CheckersMenu checkersMenu = null;

    Stage stage = null;
    GridPane grid = null;
    Scene scene = null;

    //Settings
    public String whitePlayerName = "White";
    public String blackPlayerName = "Black";
    public boolean firstStyle = true;
    public String min;

    Settings(CheckersMenu sIM){
        settings = this;
        checkersMenu = sIM;

        createWindow();
    }

    public void open(){
        stage.show();
    }

    public void close(){
        stage.hide();
    }

    private void createWindow(){

        stage = new Stage();

        grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(5);
        grid.setVgap(5);

        Label whitePlayer = new Label("White player name: ");
        Label blackPlayer = new Label("Black player name: ");
        Label timeLbl = new Label("Time for player: ");

        Label warning = new Label("Name must not have ' '.");
        warning.setFont(Font.font("Serif", 11));

        TextField whiteTxt = new TextField("White");
        TextField blackTxt = new TextField("Black");

        whitePlayer.setLabelFor(whiteTxt);
        blackPlayer.setLabelFor(blackTxt);

        ImageView first = new ImageView("file:media/Checkers/firstStyle.png");
        ImageView second = new ImageView("file:media/Checkers/secondStyle.png");

        Label lblStyle = new Label("Select background: ");

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton firstRB = new RadioButton();
        firstRB.fire();
        RadioButton secondRB = new RadioButton();

        firstRB.setToggleGroup(toggleGroup);
        secondRB.setToggleGroup(toggleGroup);

        ChoiceBox chB = new ChoiceBox();

        chB.getItems().add("5 min");
        chB.getItems().add("10 min");
        chB.getItems().add("15 min");
        chB.getItems().add("30 min");

        Button btnBack = new Button("Back");
        btnBack.setOnAction((event) ->{
            settings.close();
            checkersMenu.open();
        });

        Button btnStart = new Button("Start");
        btnStart.setOnAction((event)->{
            if((whiteTxt.getCharacters().length() > 0) && !isSpace(whiteTxt.getCharacters()) &&
                    (blackTxt.getCharacters().length() > 0) && !isSpace(blackTxt.getCharacters())) {
                whitePlayerName = whiteTxt.getText();
                blackPlayerName = blackTxt.getText();
                if (firstRB.isSelected()) firstStyle = true;
                else firstStyle = false;
                min = (String) chB.getValue();
                checkersMenu.startGame();
                settings.close();
            }
        });



        grid.add(whitePlayer, 0, 0, 2, 1);
        grid.add(blackPlayer, 0, 1, 2, 1);
        grid.add(whiteTxt, 2, 0, 2, 1);
        grid.add(blackTxt, 2, 1, 2, 1);
        grid.add(warning, 0, 2, 3, 1);
        grid.add(lblStyle, 0, 3, 3, 1);
        grid.add(first, 0, 4);
        grid.add(second, 0, 5);
        grid.add(firstRB, 1, 4);
        grid.add(secondRB, 1, 5);
        grid.add(timeLbl, 0, 6, 2, 1);
        grid.add(chB, 2, 6, 2, 1);
        grid.add(btnBack, 0, 7);
        grid.add(btnStart, 5, 7);

        scene = new Scene(grid);

        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.setResizable(false);
    }

    private void applyChanges(String resolution, String diff, boolean isFullScreen){

        File fl = new File("settings/Checkers.cfg");

        try(PrintWriter pw = new PrintWriter(fl)){

            //Resolution
            if(resolution != null && !isFullScreen) {
                int x = resolution.indexOf('x');
                pw.println("width=" + resolution.substring(0, x));
                pw.println("height=" + resolution.substring(x + 1));
            }
            else if(isFullScreen){
                pw.println("width=" + Screen.getPrimary().getBounds().getWidth());
                pw.println("height=" + Screen.getPrimary().getBounds().getHeight());
            }
            else{
                pw.println("width=" + 1000);
                pw.println("height=" + 1000);
            }

            //Difficulty level
            if(diff != null) {
                switch (diff) {
                    case "Easy":
                        pw.println("difficulty="+"EASY");
                        break;
                    case "Normal":
                        pw.println("difficulty="+"NORMAL");
                        break;
                    case "Hard":
                        pw.println("difficulty="+"HARD");
                        break;
                }
            }
            else{
                pw.println("difficulty="+"NORMAL");
            }

            //FullScreen mode
            pw.println("fullscreen="+isFullScreen);
        } catch( Exception e){
            System.out.println(e);
            System.out.println("File: Settings.java");
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
