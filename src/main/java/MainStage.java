import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

//MainStage must be initialized with stage
/*
 * Stage given in Constructor is used to:
 * - Main menu
 * - setting scenes
 *
 * But games, pauseStage, overStage and winStage have their own and new stages.
 *
 */
public class MainStage{
    //Here is stage from Constructor
    private Stage primStage;

    //Here are scenes
    private Scene menuScene;
    private Scene settingsScene;

    //Here are references to all games(at now is just one) and one reference
    //lastGame which holds last played game
    private Game lastGame;
    private SpaceInv spaceInvGame;
    private Checkers checkersGame;

    /*Here are public stages to use in every game
    *it means that we use them when we:
    *       - lost game, then we start overStage
    *       - win game, then we start winStage
    *       - pause game
    */
    public OverStage overStage;
    public WinStage winStage;
    public PauseStage pauseStage;

    private enum GAMES{ SPACEINV, CHECKERS }
    private GAMES runnedGame;

    MainStage(Stage s){
        primStage = s;
        lastGame = null;
        spaceInvGame = null;
        checkersGame = null;
        runnedGame = null;

        overStage = new OverStage();
        winStage = new WinStage();
        pauseStage = new PauseStage();

        settingsScene = createSettingsScene();
        menuScene = createMainMenuScene();

        setMainMenuScene();
    }

    private Scene createMainMenuScene(){

        //Vertical column of buttons
        VBox columnOfBtns = new VBox();
        columnOfBtns.setAlignment(Pos.CENTER);
        columnOfBtns.setSpacing(10d);
        columnOfBtns.setPrefWidth(120);

        //Text
        Label txt = new Label();
        txt.setText("Games Manager");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 20));

        //Useless for game
        /*List<String> l = Font.getFontNames();
        ChoiceBox cBox = new ChoiceBox<>(FXCollections.observableArrayList(l));
        SingleSelectionModel<String> model = cBox.getSelectionModel();
        model.selectedItemProperty().addListener((observableValue, s, t1) -> txt.setFont(Font.font(t1, 20)));*/

        //Game Space Invaders Button
        Button spaceBtn = new Button();
        spaceBtn.setText("Space Invaders");
        spaceBtn.setMinWidth(columnOfBtns.getPrefWidth());
        spaceBtn.setOnAction((event)->{
            runnedGame = GAMES.SPACEINV;
            setSettingsScene();
        });

        Button checkersBtn = new Button();
        checkersBtn.setText("Checkers");
        checkersBtn.setMinWidth(columnOfBtns.getPrefWidth());
        checkersBtn.setOnAction((event)->{
            runnedGame = GAMES.CHECKERS;
            setSettingsScene();
        });

        //Exit Button
        Button exitBtn = new Button("Exit");
        exitBtn.setMinWidth(columnOfBtns.getPrefWidth());
        exitBtn.setOnAction((event)-> Platform.exit() );

        //Add buttons to columnOfButtons
        columnOfBtns.getChildren().addAll(txt, spaceBtn, checkersBtn, exitBtn);
        //Scene
        return new Scene(columnOfBtns, 350, 400);
    }

    //If we want to invoke Main menu we can use this
    private void setMainMenuScene(){
        primStage.setResizable(false);
        primStage.setScene(menuScene);
        primStage.setTitle("SpaceInv");
        primStage.show();
    }

    //If we want to invoke settings scene we can use this(and give which game we want to run)
    private void setSettingsScene(){
        primStage.setResizable(false);
        primStage.setScene(settingsScene);
        primStage.setTitle("Game Settings");
        primStage.show();
    }

    public Scene createSettingsScene(){

        //difficulty
        //resolution
        //back Start

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(5);
        grid.setVgap(5);


        Label lblDiff = new Label("_Difficulty:");
        Label lblRes = new Label("_Size:");

        ChoiceBox chbDiff = new ChoiceBox();
        chbDiff.getItems().addAll("Easy", "Normal", "Hard");
        lblDiff.setLabelFor(chbDiff);
        lblDiff.setMnemonicParsing(true);


        ChoiceBox chbRes = new ChoiceBox();
        chbRes.getItems().addAll(Arrays.asList("1920x1080", "1680x1050", "1400x1050",
                        "1600x900", "1280x1024", "1440x900", "1280x800", "1152x864",
                        "1280x720", "1024x768", "800x600"));
        lblRes.setLabelFor(chbRes);
        lblRes.setMnemonicParsing(true);

        CheckBox checkFullScr = new CheckBox();
        checkFullScr.setText("Full Screen");

        Button btnBack = new Button("Back");
        btnBack.setOnAction((event) ->{
            setMainMenuScene();
        });

        Button btnStart = new Button("Start");
        btnStart.setOnAction((event)->{
            try {
                String res, diff;

                if(chbRes.getValue() != null) { res = chbRes.getValue().toString(); }
                else{ res = null; }

                if(chbDiff.getValue() != null){ diff = chbDiff.getValue().toString(); }
                else{ diff = null; }

                applyChanges(res, diff, checkFullScr.isSelected());

                switch(runnedGame){
                    case SPACEINV:
                            spaceInvGame = new SpaceInv(this);
                            lastGame = spaceInvGame;
                        break;
                    case CHECKERS:
                        checkersGame = new Checkers(this);
                            lastGame = checkersGame;
                         break;
                }
                lastGame.resetGame();
                lastGame.startGame();

            } catch (Exception e){
                System.out.println(e);
                System.out.println("Line: 173 File: MainStage.java");
            } finally{
                primStage.hide();
            }
        });

        grid.add(lblDiff, 0, 0);
        grid.add(chbDiff, 2, 0);
        grid.add(lblRes, 0,1);
        grid.add(chbRes, 2,1);
        grid.add(checkFullScr, 3, 1);
        grid.add(btnBack, 0, 2);
        grid.add(btnStart, 2, 2);

        return new Scene(grid);
    }

    //This method get settings and write them in appropriate file.cfg
    private void applyChanges(String resolution, String diff, boolean isFullScreen){

        File fl;
        switch(runnedGame){
            case SPACEINV:
                fl = new File("settings/SpaceInv.cfg");
                break;
            case CHECKERS:
                fl = new File("settings/Checkers.cfg");
                break;
            default:
                fl = null;
                System.out.println("it doesn't work, line: 197" + " File: MainStage.java");
        }

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
            System.out.println("Line: "+ 240);
        }
    }

    //This is that window when we pause the game
    public class PauseStage extends Stage{

    }

    //This is that window when we lost the game
    public class OverStage extends Stage{
        Scene scene;

        OverStage(){
            GridPane gP = new GridPane();
            gP.setAlignment(Pos.CENTER);
            gP.setHgap(15);
            gP.setVgap(5);


            Text txt = new Text();
            txt.setText("Game Over");
            txt.setTextAlignment(TextAlignment.CENTER);
            txt.setFont(Font.font("Serif", 25));

            Button btn1 = new Button("Again");
            btn1.setOnAction((event) ->{
                try {
                    SpaceInv.sound3.stop();
                    lastGame.resetGame();
                    lastGame.startGame();
                } catch(Exception e){
                    e.printStackTrace();
                } finally{
                    this.hide();
                }
            });

            Button btn2 = new Button("Menu");
            btn2.setOnAction((event)->{
                lastGame.gameStage.hide();
                SpaceInv.sound3.stop();
                setMainMenuScene();
                this.hide();
            });

            gP.add(txt, 0, 0, 3, 1);
            gP.add(btn1, 0, 1);
            gP.add(btn2, 2, 1);

            scene = new Scene(gP, 300, 150);
            this.setScene(scene);
            this.setTitle("Game Over");
            this.centerOnScreen();
            this.setAlwaysOnTop(true);

            //This modality locks all windows except this one,
            //so first of all we must interact with this window
            this.initModality(Modality.APPLICATION_MODAL);
        }
    }

    //This will be the window when we win the game
    public class WinStage extends Stage{
        Scene scene;

        WinStage(){
            GridPane gP = new GridPane();
            gP.setAlignment(Pos.CENTER);
            gP.setHgap(15);
            gP.setVgap(5);


            Text txt = new Text();
            txt.setText("WIN");
            txt.setTextAlignment(TextAlignment.CENTER);
            txt.setFont(Font.font("Serif", 25));

            Text txt2 = new Text();
            txt2.setText("Do you want to play again?");
            txt2.setTextAlignment(TextAlignment.CENTER);
            txt2.setFont(Font.font("Serif", 10));

            Button btn1 = new Button("Again");
            btn1.setOnAction((event) ->{
                try {
                    SpaceInv.soundWin.stop();
                    lastGame.resetGame();
                    lastGame.startGame();
                } catch(Exception e){
                    e.printStackTrace();
                } finally{
                    this.hide();
                }
            });

            Button btn2 = new Button("Menu");
            btn2.setOnAction((event)->{
                lastGame.gameStage.hide();
                SpaceInv.soundWin.stop();
                setMainMenuScene();
                this.hide();
            });

            gP.add(txt, 1, 0, 1, 1);
            gP.add(txt2, 0, 1, 3, 1);
            gP.add(btn1, 0, 2);
            gP.add(btn2, 2, 2);

            scene = new Scene(gP, 300, 150);
            this.setScene(scene);
            this.setTitle("Win");
            this.centerOnScreen();
            this.setAlwaysOnTop(true);

            //This modality locks all windows except this one,
            //so first of all we must interact with this window
            this.initModality(Modality.APPLICATION_MODAL);
        }
    }
}
