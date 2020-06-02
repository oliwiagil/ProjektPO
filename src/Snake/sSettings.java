package Snake;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class sSettings {

    sSettings settings = null;
    SnakeMenu snakeMenu = null;

    Stage stage = null;
    GridPane grid = null;
    Scene scene = null;

    sSettings(SnakeMenu sIM){
        settings = this;
        snakeMenu = sIM;

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


        Label lblDiff = new Label("_Difficulty:");

        ChoiceBox chbDiff = new ChoiceBox();
        chbDiff.getItems().addAll("Easy", "Normal", "Hard");
        lblDiff.setLabelFor(chbDiff);
        lblDiff.setMnemonicParsing(true);

        CheckBox checkBox = new CheckBox("Play with walls");

        Button btnBack = new Button("Back");
        btnBack.setOnAction((event) ->{
            settings.close();
            snakeMenu.open();
        });

        Button btnStart = new Button("Apply");
        btnStart.setOnAction((event)->{
            String diff;

            if(chbDiff.getValue() != null){ diff = chbDiff.getValue().toString(); }
            else{ diff = null; }

            applyChanges(diff,checkBox.isSelected());
            snakeMenu.game = null;
        });

        grid.add(lblDiff, 0, 0);
        grid.add(chbDiff, 2, 0);
        grid.add(checkBox, 0, 2);
        grid.add(btnBack, 0, 4);
        grid.add(btnStart, 2, 4);

        scene = new Scene(grid);

        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.setResizable(false);
    }

    private void applyChanges(String diff,boolean tryb){

        File fl = new File("settings/Snake.cfg");

        try(PrintWriter pw = new PrintWriter(fl)){
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
            if(tryb){pw.println("tryb="+"walls");}
            else{pw.println("tryb="+"normal");}

        } catch( Exception e){
            System.out.println(e);
            System.out.println("File: Settings.java");
        }
    }
}
