package SpaceInv;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        toFront();
    }

    public void close(){
        hide();
    }

    private void createWindow(){

        gameWinWindow.initStyle(StageStyle.UNDECORATED);

        Label txt = new Label();
        txt.setText("Wonderful!");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 30));

        Label txt2 = new Label("You have reached maximum score!");
        txt2.setTextAlignment(TextAlignment.CENTER);
        txt2.setFont(Font.font("Serif", 15));

        Label txt3 = new Label("What is your name?");
        txt3.setTextAlignment(TextAlignment.CENTER);
        txt3.setFont(Font.font("Serif", 15));

        VBox vBox = new VBox();

        Label warning = new Label("Name must not have ' '.");
        warning.setFont(Font.font("Serif", 11));

        TextField txtF = new TextField("Anonymous");
        vBox.getChildren().addAll(txtF, warning);
        vBox.setSpacing(5);

        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(15);
        pane.setVgap(10);

        Button btnAgain = new Button("Again");
        btnAgain.setOnAction((event) ->{

            if((txtF.getCharacters().length() > 0) && !isSpace(txtF.getCharacters())) {
                applyScore(txtF.getCharacters());
                SpaceInv.soundWin.stop();
                spaceInvMenu.startGame();
                gameWinWindow.close();
            }
        });


        Button btnMenu = new Button("Menu");
        btnMenu.setOnAction(ActionEvent->{

            if((txtF.getCharacters().length() > 0) && !isSpace(txtF.getCharacters())) {
                applyScore(txtF.getCharacters());
                spaceInvMenu.game.gameStage.hide();
                SpaceInv.soundWin.stop();
                spaceInvMenu.open();
                gameWinWindow.close();
            }
        });

        pane.add(txt, 1, 0, 4, 1);
        pane.add(txt2, 0, 1, 4, 1);
        pane.add(txt3, 0, 2, 3, 1);
        pane.add(vBox, 0, 3, 3 , 1);
        pane.add(btnAgain, 0, 4);
        pane.add(btnMenu, 3, 4);
        pane.setPadding(new Insets(15));

        scene = new Scene(pane, 350, 200);
        gameWinWindow.setScene(scene);
        gameWinWindow.setTitle("Victory");

        //This modality locks all windows except this one,
        //so first of all we must interact with this window
        gameWinWindow.initModality(Modality.APPLICATION_MODAL);
        gameWinWindow.setScene(scene);
    }

    private void applyScore(CharSequence charName){// who  howMuch    lvlOfDifficulty  when

        StringBuilder name = new StringBuilder();
        for(int i = 0; i<charName.length(); i++){
            name.append(charName.charAt(i));
        }

        File f = new File("scores/SpaceInv/scores.txt");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        try(FileWriter fW = new FileWriter(f, true)){
            fW.append(name.toString() + " " + spaceInvMenu.game.scoreI + " " + spaceInvMenu.game.difficulty
                    + " " + LocalDateTime.now().format(format)+"\n");
        } catch(Exception e){
            System.out.println("FILE: GameOverWindow.java, ERROR: Cannot apply score.");
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
